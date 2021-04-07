package io.dropwizard.cassandra;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import javax.validation.Valid;

@JsonTypeName("basic")
public class BasicCassandraFactory extends CassandraFactory {
    private static final Logger log = LoggerFactory.getLogger(BasicCassandraFactory.class);

    @Valid
    @NotEmpty
    @JsonProperty
    private List<ContactPoint> contactPoints;

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    public void setContactPoints(final List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @Override
    public CqlSession build(final MetricRegistry metrics, final LifecycleEnvironment lifecycle, final HealthCheckRegistry healthChecks,
                            final Tracing tracing) {

        // The reason this is done here and not in CassandraFactory, is to allow for client factories that might leverage service discovery
        final CqlSessionBuilder builder = setUpClusterBuilder(metrics);

        contactPoints.stream()
                .map(contactPoint -> new InetSocketAddress(contactPoint.getHost(), contactPoint.getPort()))
                .forEach(builder::addContactPoint);

        final CqlSession session = builder.build();

        setUpHealthChecks(session, healthChecks);

        setUpMetrics(session, metrics);

        setUpLifecycle(session, lifecycle);

        log.debug("Successfully setup basic Cql Session={}", session.getName());

        return session;
    }
}
