package io.dropwizard.cassandra;

import brave.Tracing;
import brave.cassandra.driver.TracingSession;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@JsonTypeName("basic")
public class BasicCassandraFactory extends CassandraFactory {
    private static final Logger log = LoggerFactory.getLogger(BasicCassandraFactory.class);

    @Valid
    @NotEmpty
    @JsonProperty
    private List<String> contactPoints;

    public List<String> getContactPoints() {
        return contactPoints;
    }

    public void setContactPoints(final List<String> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @Override
    public Session build(final MetricRegistry metrics, final LifecycleEnvironment lifecycle, final HealthCheckRegistry healthChecks,
                         final Tracing tracing) {
        // The reason this is done here and not in CassandraFactory, is to allow for client factories that might leverage service discovery
        final Cluster.Builder builder = setUpClusterBuilder(metrics)
                .withPort(getPort())
                .addContactPoints(contactPoints.toArray(new String[0]));

        final Cluster cluster = builder.build();

        final Session session = Optional.ofNullable(tracing)
                .map(t -> TracingSession.create(t, cluster.connect()))
                .orElseGet(cluster::connect);

        setUpHealthChecks(session, healthChecks);

        setUpMetrics(cluster, metrics);

        setUpLifecycle(cluster, lifecycle);

        log.debug("Successfully setup basic Cassandra cluster={}", clusterName);

        return session;
    }
}
