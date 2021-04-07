package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.internal.core.loadbalancing.BasicLoadBalancingPolicy;

public class BasicPolicyFactoryTest extends LoadBalancingTestHelper<BasicPolicyFactory> {
    @Override
    public Class<BasicPolicyFactory> getPolicyFactoryClass() {
        return BasicPolicyFactory.class;
    }

    @Override
    public String getYaml() {
        return "smoke/loadbalancing/basic.yaml";
    }

    @Override
    public Class<BasicLoadBalancingPolicy> getLoadBalancingPolicy() {
        return BasicLoadBalancingPolicy.class;
    }
}
