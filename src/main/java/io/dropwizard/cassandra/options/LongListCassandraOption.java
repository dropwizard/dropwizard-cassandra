package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("long-list")
public class LongListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<Long>> {
    @Override
    public BiConsumer<DriverOption, List<Long>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withLongList;
    }
}
