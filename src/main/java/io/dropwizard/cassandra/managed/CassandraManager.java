package io.dropwizard.cassandra.managed;

import com.datastax.driver.core.CloseFuture;
import com.datastax.driver.core.Cluster;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

public class CassandraManager implements Managed {
    private final Logger log = LoggerFactory.getLogger(CassandraManager.class);

    private final Cluster cluster;
    private final Duration shutdownGracePeriod;

    public CassandraManager(final Cluster cluster, final Duration shutdownGracePeriod) {
        this.cluster = requireNonNull(cluster);
        this.shutdownGracePeriod = requireNonNull(shutdownGracePeriod);
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void stop() throws Exception {
        log.debug("Attempting graceful shutdown of Cassandra cluster={}", cluster.getClusterName());
        final CloseFuture future = cluster.closeAsync();
        try {
            future.get(shutdownGracePeriod.toMilliseconds(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.warn("Cassandra cluster did not close in gracePeriod={}. Forcing it now.", shutdownGracePeriod);
            future.force();
        }
    }
}
