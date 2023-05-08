package io.dropwizard.cassandra.test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.test.smoke.SmokeTestApp;
import io.dropwizard.cassandra.test.smoke.SmokeTestConfiguration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class DropwizardCassandraIT {
    @Container
    public static final CassandraContainer<?> CONTAINER = new CassandraContainer<>()
            .withInitScript("test.cql");

    private static DropwizardTestSupport<SmokeTestConfiguration> app;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        InetSocketAddress contactPoint = CONTAINER.getContactPoint();
        app = new DropwizardTestSupport<>(SmokeTestApp.class, Resources.getResource("minimal.yaml").getPath(),
                ConfigOverride.config("cassandra.contactPoints[0].host", contactPoint.getHostName()),
                ConfigOverride.config("cassandra.contactPoints[0].port", String.valueOf(contactPoint.getPort())));
        app.before();
    }

    @AfterAll
    static void tearDownAfterClass() {
        app.after();
    }

    @Test
    void canQueryCassandra() {
        final CqlSession session = ((SmokeTestApp) app.getApplication()).getSession();
        final ResultSet resultSet = session.execute("SELECT * FROM system_schema.columns");
        final List<String> result = resultSet.all().stream()
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        assertThat(result, hasItem("system"));
    }

    @Test
    void cassandraMetricsArePublished() {
        assertThat(app.getEnvironment().metrics().getNames(), hasItem("s0.continuous-cql-requests"));
        assertTrue(app.getEnvironment().metrics().getNames().stream().anyMatch(x -> x.contains("bytes-sent")));
    }

    @Test
    void cassandraHealthCheckIsPublished() {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(app.getAdminPort())
                .path("healthcheck")
                .build();

        final WebTarget target = ClientBuilder.newClient().target(uri);
        final String result = target.request().get(String.class);

        assertThat(result, containsString("s0"));
    }

    @Test
    void metricsShouldBeInitialized() {
        final CqlSession session = ((SmokeTestApp) app.getApplication()).getSession();
        assertThat(session.getMetrics(), is(not(nullValue())));
    }
}
