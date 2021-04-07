package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.internal.core.loadbalancing.DcInferringLoadBalancingPolicy;

public class DataCenterInferringPolicyFactoryTest extends LoadBalancingTestHelper<DataCenterInferringPolicyFactory> {
    @Override
    public Class<DataCenterInferringPolicyFactory> getPolicyFactoryClass() {
        return DataCenterInferringPolicyFactory.class;
    }

    @Override
    public String getYaml() {
        return "smoke/loadbalancing/dc-inferring.yaml";
    }

    @Override
    public Class<DcInferringLoadBalancingPolicy> getLoadBalancingPolicy() {
        return DcInferringLoadBalancingPolicy.class;
    }
}
