package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("integer-list")
public class IntegerListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<Integer>> {
    @Override
    public BiConsumer<DriverOption, List<Integer>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withIntList;
    }
}
