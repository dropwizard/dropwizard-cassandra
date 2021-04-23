package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;

@JsonTypeName("duration-list")
public class DurationListCassandraOption extends AbstractCassandraOptionsWithBuilder<List<Duration>> {
    @Override
    public BiConsumer<DriverOption, List<Duration>> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withDurationList;
    }
}
