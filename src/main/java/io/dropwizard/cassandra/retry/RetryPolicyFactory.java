package io.dropwizard.cassandra.retry;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface RetryPolicyFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
