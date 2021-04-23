package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.internal.core.loadbalancing.DefaultLoadBalancingPolicy;

public class DefaultPolicyFactoryTest extends LoadBalancingTestHelper<DefaultPolicyFactory> {
    @Override
    public Class<DefaultPolicyFactory> getPolicyFactoryClass() {
        return DefaultPolicyFactory.class;
    }

    @Override
    public String getYaml() {
        return "smoke/loadbalancing/default.yaml";
    }

    @Override
    public Class<DefaultLoadBalancingPolicy> getLoadBalancingPolicy() {
        return DefaultLoadBalancingPolicy.class;
    }
}
