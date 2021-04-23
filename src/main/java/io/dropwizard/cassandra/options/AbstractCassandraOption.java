package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractCassandraOption<T> implements CassandraOption {
    private String name;
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
