package io.dropwizard.cassandra.reconnection;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.connection.ExponentialReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

@JsonTypeName("exponential")
public class ExponentialReconnectionPolicyFactory implements ReconnectionPolicyFactory {
    @NotNull
    @MaxDuration(value = Long.MAX_VALUE, unit = TimeUnit.MILLISECONDS)
    @JsonProperty
    private Duration baseConnectionDelay = Duration.seconds(1);
    @NotNull
    @MaxDuration(value = Long.MAX_VALUE, unit = TimeUnit.MILLISECONDS)
    @JsonProperty
    private Duration maxReconnectionDelay = Duration.seconds(60);

    public Duration getBaseConnectionDelay() {
        return baseConnectionDelay;
    }

    public void setBaseConnectionDelay(final Duration baseConnectionDelay) {
        this.baseConnectionDelay = baseConnectionDelay;
    }

    public Duration getMaxReconnectionDelay() {
        return maxReconnectionDelay;
    }

    public void setMaxReconnectionDelay(final Duration maxReconnectionDelay) {
        this.maxReconnectionDelay = maxReconnectionDelay;
    }

    @Override
    public void build(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.RECONNECTION_POLICY_CLASS, ExponentialReconnectionPolicy.class)
                .withLong(DefaultDriverOption.RECONNECTION_BASE_DELAY, getBaseConnectionDelay().toMilliseconds())
                .withLong(DefaultDriverOption.RECONNECTION_MAX_DELAY, getMaxReconnectionDelay().toMilliseconds());
    }
}
