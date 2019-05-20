package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.ErrorAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("errorAware")
public class ErrorAwarePolicyFactory implements LoadBalancingPolicyFactory {
    @Valid
    @NotNull
    @JsonProperty
    private LoadBalancingPolicyFactory subPolicy;

    @JsonProperty
    private Integer maxErrorsPerMinute;

    @JsonProperty
    private Duration retryPeriod;

    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    public Integer getMaxErrorsPerMinute() {
        return maxErrorsPerMinute;
    }

    public void setMaxErrorsPerMinute(Integer maxErrorsPerMinute) {
        this.maxErrorsPerMinute = maxErrorsPerMinute;
    }

    public Duration getRetryPeriod() {
        return retryPeriod;
    }

    public void setRetryPeriod(Duration retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    @Override
    public LoadBalancingPolicy build() {
        final ErrorAwarePolicy.Builder builder = ErrorAwarePolicy.builder(subPolicy.build());

        if (maxErrorsPerMinute != null) {
            builder.withMaxErrorsPerMinute(maxErrorsPerMinute);
        }

        if (retryPeriod != null) {
            builder.withRetryPeriod(retryPeriod.getQuantity(), retryPeriod.getUnit());
        }

        return builder.build();
    }
}
