package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotNull;

@JsonTypeName("dcAwareRoundRobin")
public class DCAwareRoundRobinPolicyFactory implements LoadBalancingPolicyFactory {
    @NotNull
    @JsonProperty
    private String localDC;

    public String getLocalDC() {
        return localDC;
    }

    public void setLocalDC(String localDC) {
        this.localDC = localDC;
    }

    @Override
    public LoadBalancingPolicy build() {
        DCAwareRoundRobinPolicy.Builder builder = DCAwareRoundRobinPolicy.builder();

        if (localDC != null) {
            builder.withLocalDc(localDC);
        }

        return builder.build();
    }
}
