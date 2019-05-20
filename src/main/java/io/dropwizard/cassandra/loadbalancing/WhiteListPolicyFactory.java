package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.WhiteListPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.net.InetSocketAddress;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("whiteList")
public class WhiteListPolicyFactory implements LoadBalancingPolicyFactory {
    @Valid
    @NotNull
    @JsonProperty
    private LoadBalancingPolicyFactory subPolicy;

    @NotNull
    @JsonProperty
    private Collection<InetSocketAddress> whiteList;

    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    public Collection<InetSocketAddress> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(Collection<InetSocketAddress> whiteList) {
        this.whiteList = whiteList;
    }

    @Override
    public LoadBalancingPolicy build() {
        return new WhiteListPolicy(subPolicy.build(), whiteList);
    }
}
