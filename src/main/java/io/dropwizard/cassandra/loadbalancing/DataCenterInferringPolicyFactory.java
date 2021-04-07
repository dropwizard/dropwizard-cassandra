package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.loadbalancing.DcInferringLoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("data-center-inferring")
public class DataCenterInferringPolicyFactory extends BasicPolicyFactory {

    @Override
    public void build(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        super.build(builder);
        builder.withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, DcInferringLoadBalancingPolicy.class);
    }
}
