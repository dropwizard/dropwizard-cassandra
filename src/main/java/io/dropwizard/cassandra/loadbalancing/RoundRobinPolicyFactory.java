package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("roundRobin")
public class RoundRobinPolicyFactory implements LoadBalancingPolicyFactory {
    @Override
    public LoadBalancingPolicy build() {
        return new RoundRobinPolicy();
    }
}
