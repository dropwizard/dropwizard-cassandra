package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonTypeName("constant")
public class ConstantSpeculativeExecutionPolicyFactory implements SpeculativeExecutionPolicyFactory {
    @NotNull
    @JsonProperty
    private Duration delay;

    @Min(1)
    @NotNull
    @JsonProperty
    private Integer maxSpeculativeExecutions;

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public Integer getMaxSpeculativeExecutions() {
        return maxSpeculativeExecutions;
    }

    public void setMaxSpeculativeExecutions(Integer maxSpeculativeExecutions) {
        this.maxSpeculativeExecutions = maxSpeculativeExecutions;
    }

    @Override
    public SpeculativeExecutionPolicy build() {
        return new ConstantSpeculativeExecutionPolicy(delay.toMilliseconds(), maxSpeculativeExecutions);
    }
}
