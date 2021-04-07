package io.dropwizard.cassandra.test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.test.smoke.SmokeTestApp;
import io.dropwizard.cassandra.test.smoke.SmokeTestConfiguration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * TODO: Unfortunately these tests don't work with cassandraunit, and cassandraunit seems unmaintained.
 * Perhaps an alternative approach will need to be taken for these tests.
 */
public class DropwizardCassandraIT {
    @ClassRule
    public static final CassandraCQLUnit CASSANDRA = new CassandraCQLUnit(new ClassPathCQLDataSet("test.cql"),
            EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE, 30000L, 30000);

    private static DropwizardTestSupport<SmokeTestConfiguration> app;

    static {
        System.setProperty("cassandra.storagedir", "target/embeddedCassandra/cdc");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final String cassandraAddress = CASSANDRA.getSession()
                .getMetadata()
                .getNodes()
                .values()
                .toArray()[0]
                .toString();
        app = new DropwizardTestSupport<>(SmokeTestApp.class, Resources.getResource("minimal.yaml").getPath(),
                ConfigOverride.config("contactPoints", cassandraAddress));
        app.before();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        app.after();
    }

    @Test
    @Ignore
    public void canQueryCassandra() {
        final CqlSession session = ((SmokeTestApp) app.getApplication()).getSession();
        final ResultSet resultSet = session.execute("SELECT * FROM system_schema.columns");
        final List<String> result = resultSet.all().stream()
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        assertThat(result, hasItem("system"));
    }

    @Test
    @Ignore
    public void cassandraMetricsArePublished() {
        assertThat(app.getEnvironment().metrics().getNames(), hasItem("cassandra.test-cluster.authentication-errors"));
    }

    @Test
    @Ignore
    public void cassandraHealthCheckIsPublished() {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(app.getAdminPort())
                .path("healthcheck")
                .build();

        final WebTarget target = ClientBuilder.newClient().target(uri);
        final String result = target.request().get(String.class);

        assertThat(result, containsString("cassandra.test-cluster"));
    }

    @Test
    @Ignore
    public void metricsShouldBeInitialized() {
        final CqlSession session = ((SmokeTestApp) app.getApplication()).getSession();
        assertThat(session.getMetrics(), is(not(nullValue())));
    }
}
