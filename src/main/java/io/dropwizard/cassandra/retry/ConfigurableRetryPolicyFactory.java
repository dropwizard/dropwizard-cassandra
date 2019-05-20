package io.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.Min;

/**
 * A factory for configuring and building {@link ConfigurableRetryPolicy} instances.
 */
@JsonTypeName("configurable")
public class ConfigurableRetryPolicyFactory implements RetryPolicyFactory {
    @Min(0)
    @JsonProperty
    private int readTimeoutRetries = 1;
    @Min(0)
    @JsonProperty
    private int writeTimeoutRetries = 1;
    @Min(0)
    @JsonProperty
    private int unavailableRetries = 1;

    public Integer getWriteTimeoutRetries() {
        return writeTimeoutRetries;
    }

    public void setWriteTimeoutRetries(final Integer writeTimeoutRetries) {
        this.writeTimeoutRetries = writeTimeoutRetries;
    }

    public Integer getUnavailableRetries() {
        return unavailableRetries;
    }

    public void setUnavailableRetries(final Integer unavailableRetries) {
        this.unavailableRetries = unavailableRetries;
    }

    public Integer getReadTimeoutRetries() {
        return readTimeoutRetries;
    }

    public void setReadTimeoutRetries(final Integer readTimeoutRetries) {
        this.readTimeoutRetries = readTimeoutRetries;
    }

    @Override
    public RetryPolicy build() {
        return new ConfigurableRetryPolicy(readTimeoutRetries, writeTimeoutRetries, unavailableRetries);
    }
}
