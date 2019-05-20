package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("latencyAware")
public class LatencyAwarePolicyFactory implements LoadBalancingPolicyFactory {
    @Valid
    @NotNull
    @JsonProperty
    private LoadBalancingPolicyFactory subPolicy;
    @JsonProperty
    private Double exclusionThreshold;
    @JsonProperty
    private Integer minimumMeasurements;
    @JsonProperty
    private Duration retryPeriod;
    @JsonProperty
    private Duration scale;
    @JsonProperty
    private Duration updateRate;

    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    public Double getExclusionThreshold() {
        return exclusionThreshold;
    }

    public void setExclusionThreshold(Double exclusionThreshold) {
        this.exclusionThreshold = exclusionThreshold;
    }

    public Integer getMinimumMeasurements() {
        return minimumMeasurements;
    }

    public void setMinimumMeasurements(Integer minimumMeasurements) {
        this.minimumMeasurements = minimumMeasurements;
    }

    public Duration getRetryPeriod() {
        return retryPeriod;
    }

    public void setRetryPeriod(Duration retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    public Duration getScale() {
        return scale;
    }

    public void setScale(Duration scale) {
        this.scale = scale;
    }

    public Duration getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(Duration updateRate) {
        this.updateRate = updateRate;
    }

    @Override
    public LoadBalancingPolicy build() {
        LatencyAwarePolicy.Builder builder = LatencyAwarePolicy.builder(subPolicy.build());

        if (exclusionThreshold != null) {
            builder.withExclusionThreshold(exclusionThreshold);
        }

        if (minimumMeasurements != null) {
            builder.withMininumMeasurements(minimumMeasurements);
        }

        if (retryPeriod != null) {
            builder.withRetryPeriod(retryPeriod.getQuantity(), retryPeriod.getUnit());
        }

        if (scale != null) {
            builder.withScale(scale.getQuantity(), scale.getUnit());
        }

        if (updateRate != null) {
            builder.withUpdateRate(updateRate.getQuantity(), updateRate.getUnit());
        }

        return builder.build();
    }
}
