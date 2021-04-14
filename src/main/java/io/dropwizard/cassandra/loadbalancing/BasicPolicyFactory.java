package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.loadbalancing.BasicLoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("basic")
public class BasicPolicyFactory implements LoadBalancingPolicyFactory {
    @JsonProperty
    private String localDataCenter;
    @JsonProperty
    private Boolean dataCenterFailoverAllowLocalConsistencyLevels;
    @JsonProperty
    private Boolean slowAvoidance;
    @JsonProperty
    private Integer dcFailoverMaxNodesPerRemoteDc;

    public Boolean getDataCenterFailoverAllowLocalConsistencyLevels() {
        return dataCenterFailoverAllowLocalConsistencyLevels;
    }

    public void setDataCenterFailoverAllowLocalConsistencyLevels(Boolean dataCenterFailoverAllowLocalConsistencyLevels) {
        this.dataCenterFailoverAllowLocalConsistencyLevels = dataCenterFailoverAllowLocalConsistencyLevels;
    }

    public Boolean getSlowAvoidance() {
        return slowAvoidance;
    }

    public void setSlowAvoidance(Boolean slowAvoidance) {
        this.slowAvoidance = slowAvoidance;
    }

    public Integer getDcFailoverMaxNodesPerRemoteDc() {
        return dcFailoverMaxNodesPerRemoteDc;
    }

    public void setDcFailoverMaxNodesPerRemoteDc(Integer dcFailoverMaxNodesPerRemoteDc) {
        this.dcFailoverMaxNodesPerRemoteDc = dcFailoverMaxNodesPerRemoteDc;
    }

    public String getLocalDataCenter() {
        return localDataCenter;
    }

    public void setLocalDataCenter(String localDataCenter) {
        this.localDataCenter = localDataCenter;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, BasicLoadBalancingPolicy.class)
                .withNullSafeBoolean(DefaultDriverOption.LOAD_BALANCING_DC_FAILOVER_ALLOW_FOR_LOCAL_CONSISTENCY_LEVELS,
                        dataCenterFailoverAllowLocalConsistencyLevels)
                .withNullSafeInteger(DefaultDriverOption.LOAD_BALANCING_DC_FAILOVER_MAX_NODES_PER_REMOTE_DC,
                        dcFailoverMaxNodesPerRemoteDc)
                .withNullSafeString(DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, localDataCenter)
                .withNullSafeBoolean(DefaultDriverOption.LOAD_BALANCING_POLICY_SLOW_AVOIDANCE, slowAvoidance);
    }
}
