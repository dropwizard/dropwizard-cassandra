package io.dropwizard.cassandra.protocolVersion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ProtocolVersionFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
