package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("string-list")
public class StringListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<String>> {
    @Override
    public BiConsumer<DriverOption, List<String>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withStringList;
    }
}
