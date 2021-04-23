package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.specex.ConstantSpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
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
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS, ConstantSpeculativeExecutionPolicy.class)
                .withInt(DefaultDriverOption.SPECULATIVE_EXECUTION_MAX, maxSpeculativeExecutions)
                .withNullSafeDuration(DefaultDriverOption.SPECULATIVE_EXECUTION_DELAY, delay);
    }
}
