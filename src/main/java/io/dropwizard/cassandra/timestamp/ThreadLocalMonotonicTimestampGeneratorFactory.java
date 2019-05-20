package io.dropwizard.cassandra.timestamp;

import com.datastax.driver.core.ThreadLocalMonotonicTimestampGenerator;
import com.datastax.driver.core.TimestampGenerator;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link ThreadLocalMonotonicTimestampGenerator} instances.
 */
@JsonTypeName("thread")
public class ThreadLocalMonotonicTimestampGeneratorFactory implements TimestampGeneratorFactory {
    @Override
    public TimestampGenerator build() {
        return new ThreadLocalMonotonicTimestampGenerator();
    }
}
