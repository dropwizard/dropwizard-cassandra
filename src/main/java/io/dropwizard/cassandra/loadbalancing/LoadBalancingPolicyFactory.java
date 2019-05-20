package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface LoadBalancingPolicyFactory extends Discoverable {
    LoadBalancingPolicy build();
}
