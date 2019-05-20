package io.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

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
    public ReconnectionPolicy build() {
        return new ConstantReconnectionPolicy(delay.toMilliseconds());
    }
}
