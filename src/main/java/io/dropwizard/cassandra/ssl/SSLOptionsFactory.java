package io.dropwizard.cassandra.ssl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SSLOptionsFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
