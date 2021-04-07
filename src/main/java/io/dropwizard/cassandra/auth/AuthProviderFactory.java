package io.dropwizard.cassandra.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface AuthProviderFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
