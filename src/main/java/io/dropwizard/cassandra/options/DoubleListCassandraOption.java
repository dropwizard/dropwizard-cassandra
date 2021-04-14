package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("double-list")
public class DoubleListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<Double>> {
    @Override
    public BiConsumer<DriverOption, List<Double>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withDoubleList;
    }
}
