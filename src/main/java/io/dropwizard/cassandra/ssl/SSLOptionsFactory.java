package io.dropwizard.cassandra.ssl;

import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SSLOptionsFactory extends Discoverable {
    SSLOptions build();
}
