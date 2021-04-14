package io.dropwizard.cassandra.options;

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CassandraOption extends Discoverable, DropwizardCassandraConfigBuilder {
    default DriverOption getDriverOption(String name) {
        return () -> name;
    }
}
