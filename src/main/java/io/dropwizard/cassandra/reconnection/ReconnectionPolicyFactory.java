package io.dropwizard.cassandra.reconnection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ReconnectionPolicyFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
