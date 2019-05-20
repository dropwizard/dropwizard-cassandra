package io.dropwizard.cassandra.retry;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.WriteType;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.ReadFailureException;
import com.datastax.driver.core.exceptions.WriteFailureException;
import com.datastax.driver.core.policies.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A configable retry policy.
 */
public class ConfigurableRetryPolicy implements RetryPolicy {
    private static final Logger log = LoggerFactory.getLogger(ConfigurableRetryPolicy.class);

    private static final int DEFAULT_RETRY_COUNT = 1;

    private QueryOptions queryOptions;

    private final int readTimeoutRetries;
    private final int writeTimeoutRetries;
    private final int unavailableRetries;

    public ConfigurableRetryPolicy(final int readTimeoutRetries, final int writeTimeoutRetries,
                                   final int unavailableRetries) {
        checkArgument(readTimeoutRetries >= 0, "readTimeoutRetries must be non-negative: "
                + readTimeoutRetries);
        checkArgument(writeTimeoutRetries >= 0, "writeTimeoutRetries must be non-negative: "
                + writeTimeoutRetries);
        checkArgument(unavailableRetries >= 0, "unavailableRetries must be non-negative: "
                + unavailableRetries);
        this.readTimeoutRetries = readTimeoutRetries;
        this.writeTimeoutRetries = writeTimeoutRetries;
        this.unavailableRetries = unavailableRetries;
    }

    public ConfigurableRetryPolicy() {
        this.readTimeoutRetries = DEFAULT_RETRY_COUNT;
        this.writeTimeoutRetries = DEFAULT_RETRY_COUNT;
        this.unavailableRetries = DEFAULT_RETRY_COUNT;
    }

    @Override
    public RetryDecision onWriteTimeout(final Statement statement, final ConsistencyLevel cl,
                                        final WriteType writeType, final int requiredAcks, final int receivedAcks,
                                        final int nbRetry) {
        if (nbRetry < writeTimeoutRetries && isIdempotent(statement)) {
            log.debug("Retrying write timed out after tries={} idempotent={} statement='{}'", nbRetry + 1, statement.isIdempotent(),
                    statement);
            return RetryDecision.retry(null);
        } else {
            return RetryDecision.rethrow();
        }
    }

    @Override
    public RetryDecision onReadTimeout(final Statement statement,
                                       final ConsistencyLevel cl,
                                       final int requiredResponses,
                                       final int receivedResponses,
                                       final boolean dataRetrieved,
                                       final int nbRetry) {
        if (nbRetry < readTimeoutRetries) {
            log.debug("Retrying a read timed out after tries={} statement=\'{}\'", nbRetry + 1, statement);
            return RetryDecision.retry(null);
        } else {
            return RetryDecision.rethrow();
        }
    }

    @Override
    public RetryDecision onUnavailable(final Statement statement,
                                       final ConsistencyLevel cl,
                                       final int requiredReplica,
                                       final int aliveReplica,
                                       final int nbRetry) {
        if (nbRetry < unavailableRetries) {
            log.info("Retrying unavailable after tries={} statement=\'{}\'", nbRetry + 1, statement);
            return RetryDecision.tryNextHost(null);
        } else {
            return RetryDecision.rethrow();
        }
    }

    protected boolean isIdempotent(final Statement statement) {

        return Optional.ofNullable(statement)
                .map(Statement::isIdempotent)
                .orElse(queryOptions.getDefaultIdempotence());
    }

    @Override
    public void init(final Cluster cluster) {
        if (cluster != null) {
            this.queryOptions = cluster.getConfiguration().getQueryOptions();
        }
    }

    @Override
    public RetryDecision onRequestError(Statement statement, ConsistencyLevel cl, DriverException e, int nbRetry) {
        return !(e instanceof WriteFailureException) &&
                !(e instanceof ReadFailureException) ? RetryDecision.tryNextHost(cl) : RetryDecision.rethrow();
    }

    @Override
    public void close() {}

    public int getReadTimeoutRetries() {
        return readTimeoutRetries;
    }

    public int getWriteTimeoutRetries() {
        return writeTimeoutRetries;
    }

    public int getUnavailableRetries() {
        return unavailableRetries;
    }
}
