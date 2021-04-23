package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.loadbalancing.DefaultLoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("default")
public class DefaultPolicyFactory extends BasicPolicyFactory {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        super.accept(builder);
        builder.withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, DefaultLoadBalancingPolicy.class);
    }
}
