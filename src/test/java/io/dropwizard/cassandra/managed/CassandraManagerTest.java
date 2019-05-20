package io.dropwizard.cassandra.managed;

import com.datastax.driver.core.CloseFuture;
import com.datastax.driver.core.Cluster;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CassandraManagerTest {
    private static final Duration GRACEFUL_SHUTDOWN_DURATION = Duration.seconds(1);

    private final Cluster cluster = mock(Cluster.class);
    private final CassandraManager cassandraManager = new CassandraManager(cluster, GRACEFUL_SHUTDOWN_DURATION);

    @Before
    public void setUp() {
        reset(cluster);
    }

    @Test
    public void shouldShutdownGracefully() throws Exception {
        final CloseFuture future = mock(CloseFuture.class);
        when(cluster.closeAsync()).thenReturn(future);

        when(future.get(eq(GRACEFUL_SHUTDOWN_DURATION.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(null);

        cassandraManager.stop();

        verify(future).get(GRACEFUL_SHUTDOWN_DURATION.toMilliseconds(), TimeUnit.MILLISECONDS);
    }

    @Test
    public void shouldForceShutdownWhenGracefulTimeExceeded() throws Exception {
        final CloseFuture future = mock(CloseFuture.class);
        when(cluster.closeAsync()).thenReturn(future);

        when(future.get(eq(GRACEFUL_SHUTDOWN_DURATION.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(new TimeoutException());

        cassandraManager.stop();

        verify(future).force();
    }
}
