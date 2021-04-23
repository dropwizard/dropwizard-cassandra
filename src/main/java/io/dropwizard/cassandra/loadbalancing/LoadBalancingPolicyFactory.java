package io.dropwizard.cassandra.loadbalancing;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface LoadBalancingPolicyFactory extends Discoverable, DropwizardCassandraConfigBuilder {
}
