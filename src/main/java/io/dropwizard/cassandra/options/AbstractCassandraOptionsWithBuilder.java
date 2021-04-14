package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;

public abstract class AbstractCassandraOptionsWithBuilder<T> extends AbstractCassandraOption<T> {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        getConfigConsumer(builder).accept(getDriverOption(getName()), getValue());
    }

    public abstract BiConsumer<DriverOption, T> getConfigConsumer(DropwizardProgrammaticDriverConfigLoaderBuilder builder);
}
