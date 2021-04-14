package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.time.Duration;
import java.util.function.BiConsumer;

@JsonTypeName("duration")
public class DurationCassandraOption extends AbstractCassandraOptionsWithBuilder<Duration> {
    @Override
    public BiConsumer<DriverOption, Duration> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        return builder::withDuration;
    }
}
