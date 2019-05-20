package io.dropwizard.cassandra.timestamp;

import com.datastax.driver.core.AtomicMonotonicTimestampGenerator;
import com.datastax.driver.core.TimestampGenerator;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link AtomicMonotonicTimestampGenerator} instances.
 */
@JsonTypeName("atomic")
public class AtomicMonotonicTimestampGeneratorFactory implements TimestampGeneratorFactory {
    @Override
    public TimestampGenerator build() {
        return new AtomicMonotonicTimestampGenerator();
    }
}
