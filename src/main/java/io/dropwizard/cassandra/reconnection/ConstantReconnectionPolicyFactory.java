package io.dropwizard.cassandra.reconnection;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.connection.ConstantReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;

import java.util.concurrent.TimeUnit;

import jakarta.validation.constraints.NotNull;

@JsonTypeName("constant")
public class ConstantReconnectionPolicyFactory implements ReconnectionPolicyFactory {
    @NotNull
    @MaxDuration(value = Long.MAX_VALUE, unit = TimeUnit.MILLISECONDS)
    @JsonProperty
    private Duration delay = Duration.seconds(3);

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(final Duration delay) {
        this.delay = delay;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.RECONNECTION_POLICY_CLASS, ConstantReconnectionPolicy.class);
        builder.withNullSafeDuration(DefaultDriverOption.RECONNECTION_BASE_DELAY, getDelay());
    }
}
