package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import java.util.function.BiConsumer;

@JsonTypeName("bytes")
public class BytesCassandraOption extends AbstractCassandraOptionsWithBuilder<Long> {
    @Override
    public BiConsumer<DriverOption, Long> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withBytes;
    }
}
