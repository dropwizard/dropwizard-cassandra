package io.dropwizard.cassandra.health;

import com.codahale.metrics.health.HealthCheck;
import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.servererrors.ReadFailureException;
import io.dropwizard.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

class CassandraHealthCheckTest {
    private static final String QUERY = "SELECT key FROM system.local;";
    private static final Duration TIMEOUT = Duration.seconds(5);

    private final CqlSession session = mock(CqlSession.class);
    private final CassandraHealthCheck check = new CassandraHealthCheck(session, QUERY, TIMEOUT);
    final CompletableFuture future = mock(CompletableFuture.class);

    @BeforeEach
    void setUp() {
        reset(session);
        when(future.toCompletableFuture()).thenReturn(future);
        when(session.executeAsync(eq(QUERY))).thenReturn(future);
    }

    @Test
    void checkShouldReturnHealthyIfSucceeds() throws Exception {
        when(future.get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(mock(ResultSet.class));

        assertThat(check.check().isHealthy()).isTrue();
    }

    @Test
    void checkShouldReturnUnhealthyIfFailsDueToTimeout() throws Exception {
        final TimeoutException exception = new TimeoutException();
        when(future.get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(exception);

        final HealthCheck.Result result = check.check();

        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getError()).isEqualTo(exception);
    }

    @Test
    void checkShouldReturnUnhealthyIfFailsDueToCassandraError() throws Exception {
        final ReadFailureException exception =
                new ReadFailureException(null, ConsistencyLevel.ONE, 0, 1, 1,
                        false, Collections.singletonMap(InetAddress.getByName("127.0.0.1"), 1));
        when(future.toCompletableFuture().get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(exception);

        final HealthCheck.Result result = check.check();

        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getError()).isEqualTo(exception);
    }
}
