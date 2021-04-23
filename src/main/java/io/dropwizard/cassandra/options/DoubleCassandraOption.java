package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;

@JsonTypeName("double")
public class DoubleCassandraOption extends AbstractCassandraOptionsWithBuilder<Double> {
    @Override
    public BiConsumer<DriverOption, Double> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withDouble;
    }
}
