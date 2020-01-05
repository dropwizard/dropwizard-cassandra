package io.dropwizard.cassandra.health;

import com.codahale.metrics.health.HealthCheck;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.ReadFailureException;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class CassandraHealthCheckTest {
    private static final String QUERY = "SELECT key FROM system.local;";
    private static final Duration TIMEOUT = Duration.seconds(5);

    private final Session session = mock(Session.class);
    private final CassandraHealthCheck check = new CassandraHealthCheck(session, QUERY, TIMEOUT);

    @Before
    public void setUp() {
        reset(session);
    }

    @Test
    public void checkShouldReturnHealthyIfSucceeds() throws Exception {
        final ResultSetFuture future = mock(ResultSetFuture.class);
        when(session.executeAsync(eq(QUERY))).thenReturn(future);

        when(future.get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(mock(ResultSet.class));

        assertThat(check.check().isHealthy()).isTrue();
    }

    @Test
    public void checkShouldReturnUnhealthyIfFailsDueToTimeout() throws Exception {
        final ResultSetFuture future = mock(ResultSetFuture.class);
        when(session.executeAsync(eq(QUERY))).thenReturn(future);
        final Cluster cluster = mock(Cluster.class);
        when(session.getCluster()).thenReturn(cluster);
        when(cluster.getClusterName()).thenReturn("test");

        final TimeoutException exception = new TimeoutException();
        when(future.get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(exception);

        final HealthCheck.Result result = check.check();

        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getError()).isEqualTo(exception);
    }

    @Test
    public void checkShouldReturnUnhealthyIfFailsDueToCassandraError() throws Exception {
        final ResultSetFuture future = mock(ResultSetFuture.class);
        when(session.executeAsync(eq(QUERY))).thenReturn(future);
        final Cluster cluster = mock(Cluster.class);
        when(session.getCluster()).thenReturn(cluster);
        when(cluster.getClusterName()).thenReturn("test");

        final InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9042);
        final ReadFailureException exception =
                new ReadFailureException(() -> inetSocketAddress, ConsistencyLevel.ONE, 0, 1, 1,
                        Collections.singletonMap(InetAddress.getByName("127.0.0.1"), 1), false);
        when(future.get(eq(TIMEOUT.toMilliseconds()), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(exception);

        final HealthCheck.Result result = check.check();

        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getError()).isEqualTo(exception);
    }
}
