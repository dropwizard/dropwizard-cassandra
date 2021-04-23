package io.dropwizard.cassandra.timestamp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface TimestampGeneratorFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
