package io.dropwizard.cassandra.request;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.util.Duration;

public class RequestOptionsFactory implements DropwizardCassandraConfigBuilder {
    protected Duration requestTimeout;
    protected String requestConsistency;
    protected Integer requestPageSize;
    protected String requestSerialConsistency;
    protected Boolean requestDefaultIdempotence;

    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getRequestConsistency() {
        return requestConsistency;
    }

    public void setRequestConsistency(String requestConsistency) {
        this.requestConsistency = requestConsistency;
    }

    public Integer getRequestPageSize() {
        return requestPageSize;
    }

    public void setRequestPageSize(Integer requestPageSize) {
        this.requestPageSize = requestPageSize;
    }

    public String getRequestSerialConsistency() {
        return requestSerialConsistency;
    }

    public void setRequestSerialConsistency(String requestSerialConsistency) {
        this.requestSerialConsistency = requestSerialConsistency;
    }

    public Boolean getRequestDefaultIdempotence() {
        return requestDefaultIdempotence;
    }

    public void setRequestDefaultIdempotence(Boolean requestDefaultIdempotence) {
        this.requestDefaultIdempotence = requestDefaultIdempotence;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withNullSafeDuration(DefaultDriverOption.REQUEST_TIMEOUT, requestTimeout)
                .withNullSafeBoolean(DefaultDriverOption.REQUEST_DEFAULT_IDEMPOTENCE, requestDefaultIdempotence)
                .withNullSafeString(DefaultDriverOption.REQUEST_CONSISTENCY, requestConsistency)
                .withNullSafeInteger(DefaultDriverOption.REQUEST_PAGE_SIZE, requestPageSize)
                .withNullSafeString(DefaultDriverOption.REQUEST_SERIAL_CONSISTENCY, requestSerialConsistency);
    }
}
