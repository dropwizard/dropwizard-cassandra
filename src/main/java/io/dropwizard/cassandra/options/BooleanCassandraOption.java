package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;

@JsonTypeName("boolean")
public class BooleanCassandraOption extends AbstractCassandraOptionsWithBuilder<Boolean> {
    @Override
    public BiConsumer<DriverOption, Boolean> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withBoolean;
    }
}
