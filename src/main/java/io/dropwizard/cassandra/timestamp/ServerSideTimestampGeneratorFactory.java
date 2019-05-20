package io.dropwizard.cassandra.timestamp;

import com.datastax.driver.core.ServerSideTimestampGenerator;
import com.datastax.driver.core.TimestampGenerator;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 ** A factory for configuring and building {@link ServerSideTimestampGenerator} instances.
 */
@JsonTypeName("server")
public class ServerSideTimestampGeneratorFactory implements TimestampGeneratorFactory {
    @Override
    public TimestampGenerator build() {
        return ServerSideTimestampGenerator.INSTANCE;
    }
}
