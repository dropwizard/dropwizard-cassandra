package io.dropwizard.cassandra.managed;

import com.datastax.oss.driver.api.core.CqlSession;
import io.dropwizard.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CassandraManagerTest {
    private static final Duration GRACEFUL_SHUTDOWN_DURATION = Duration.seconds(1);

    private final CqlSession cluster = mock(CqlSession.class);
    private final CassandraManager cassandraManager = new CassandraManager(cluster, GRACEFUL_SHUTDOWN_DURATION);

    @BeforeEach
    void setUp() {
        reset(cluster);
    }

    @Test
    void shouldShutdownGracefully() throws Exception {
        final CompletableFuture<Void> future = mock(CompletableFuture.class);
        when(cluster.closeAsync()).thenReturn(future);
        when(future.toCompletableFuture()).thenReturn(future);
        cassandraManager.stop();
        verify(future).get(GRACEFUL_SHUTDOWN_DURATION.toMilliseconds(), TimeUnit.MILLISECONDS);
    }
}
