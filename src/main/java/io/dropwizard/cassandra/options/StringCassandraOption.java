package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;

@JsonTypeName("string")
public class StringCassandraOption extends AbstractCassandraOptionsWithBuilder<String> {
    @Override
    public BiConsumer<DriverOption, String> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withString;
    }
}
