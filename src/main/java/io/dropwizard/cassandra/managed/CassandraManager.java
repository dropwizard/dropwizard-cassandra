package io.dropwizard.cassandra.managed;

import com.datastax.oss.driver.api.core.CqlSession;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

public class CassandraManager implements Managed {
    private final Logger log = LoggerFactory.getLogger(CassandraManager.class);

    private final CqlSession session;
    private final Duration shutdownGracePeriod;

    public CassandraManager(final CqlSession session, final Duration shutdownGracePeriod) {
        this.session = requireNonNull(session);
        this.shutdownGracePeriod = requireNonNull(shutdownGracePeriod);
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void stop() throws Exception {
        log.debug("Attempting graceful shutdown of Cassandra Session={}", session.getName());
        final CompletableFuture<Void> future = session.closeAsync().toCompletableFuture();
        try {
            future.toCompletableFuture().get(shutdownGracePeriod.toMilliseconds(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.warn("Cassandra cluster did not close in gracePeriod={}.", shutdownGracePeriod);
            try {
                session.forceCloseAsync().toCompletableFuture()
                        .get(shutdownGracePeriod.toMilliseconds(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException te) {
                log.warn("Force closing Cassandra cluster did not happen in gracePeriod={}.", shutdownGracePeriod);
            }
        }
    }
}
