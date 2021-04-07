package io.dropwizard.cassandra.timestamp;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.time.ServerSideTimestampGenerator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

/**
 ** A factory for configuring and building {@link ServerSideTimestampGenerator} instances.
 */
@JsonTypeName("server")
public class ServerSideTimestampGeneratorFactory implements TimestampGeneratorFactory {
    @Override
    public void build(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS, ServerSideTimestampGenerator.class);
    }
}
