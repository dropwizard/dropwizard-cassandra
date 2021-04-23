package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;

@JsonTypeName("integer")
public class IntegerCassandraOption extends AbstractCassandraOptionsWithBuilder<Integer> {
    @Override
    public BiConsumer<DriverOption, Integer> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withInt;
    }
}
