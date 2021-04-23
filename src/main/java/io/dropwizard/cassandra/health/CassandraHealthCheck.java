package io.dropwizard.cassandra.health;

import com.codahale.metrics.health.HealthCheck;
import com.datastax.oss.driver.api.core.CqlSession;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class CassandraHealthCheck extends HealthCheck {
    private static final Logger log = LoggerFactory.getLogger(CassandraHealthCheck.class);

    private final CqlSession session;
    private final String validationQuery;
    private final Duration timeout;

    public CassandraHealthCheck(final CqlSession session, final String validationQuery, final Duration timeout) {
        this.session = requireNonNull(session);
        this.validationQuery = requireNonNull(validationQuery);
        this.timeout = requireNonNull(timeout);
    }

    @Override
    protected Result check() throws Exception {
        try {
            session.executeAsync(validationQuery).toCompletableFuture().get(timeout.toMilliseconds(), TimeUnit.MILLISECONDS);
            return Result.healthy();
        } catch (final Exception e) {
            log.error("Unable to connect to Cassandra [{}]", session.getName(), e);
            return Result.unhealthy(e);
        }
    }
}
