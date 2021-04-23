package io.dropwizard.cassandra.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("class")
public class ClassCassandraOption extends AbstractCassandraOption<String> {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        try {
            builder.withClass(getDriverOption(getName()), Class.forName(getValue()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
