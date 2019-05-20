package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("tokenAware")
public class TokenAwarePolicyFactory implements LoadBalancingPolicyFactory {
    @Valid
    @NotNull
    @JsonProperty
    private LoadBalancingPolicyFactory subPolicy;

    @NotNull
    @JsonProperty
    private TokenAwarePolicy.ReplicaOrdering replicaOrdering = TokenAwarePolicy.ReplicaOrdering.RANDOM;

    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    public TokenAwarePolicy.ReplicaOrdering getReplicaOrdering() {
        return replicaOrdering;
    }

    public void setReplicaOrdering(final TokenAwarePolicy.ReplicaOrdering replicaOrdering) {
        this.replicaOrdering = replicaOrdering;
    }

    @Override
    public LoadBalancingPolicy build() {
        return new TokenAwarePolicy(subPolicy.build(), replicaOrdering);
    }
}
