package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("boolean-list")
public class BooleanListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<Boolean>> {
    @Override
    public BiConsumer<DriverOption, List<Boolean>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withBooleanList;
    }
}
