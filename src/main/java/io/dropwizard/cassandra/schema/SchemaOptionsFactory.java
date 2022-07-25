package io.dropwizard.cassandra.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import static com.datastax.oss.driver.api.core.config.DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL;
import static com.datastax.oss.driver.api.core.config.DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT;
import static com.datastax.oss.driver.api.core.config.DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN;

public class SchemaOptionsFactory implements DropwizardCassandraConfigBuilder {
    // Default as defined at https://github.com/datastax/java-driver/blob/f08db2ef5fcc70b3486bd0b9ad74e9356a1be7bc/core/src/main/java/com/datastax/oss/driver/api/core/config/OptionsMap.java#L358
    public static final Integer DEFAULT_SCHEMA_AGREEMENT_INTERVAL_MILLISECONDS = 200;
    // Default as defined at https://github.com/datastax/java-driver/blob/f08db2ef5fcc70b3486bd0b9ad74e9356a1be7bc/core/src/main/java/com/datastax/oss/driver/api/core/config/OptionsMap.java#L359
    public static final Integer DEFAULT_SCHEMA_AGREEMENT_TIMEOUT_SECONDS = 10;
    // Default as defined at https://github.com/datastax/java-driver/blob/f08db2ef5fcc70b3486bd0b9ad74e9356a1be7bc/core/src/main/java/com/datastax/oss/driver/api/core/config/OptionsMap.java#L360
    public static final Boolean DEFAULT_SCHEMA_AGREEMENT_WARN_ON_FAILURE = Boolean.TRUE;

    @JsonProperty
    private Integer agreementTimeoutSeconds = DEFAULT_SCHEMA_AGREEMENT_TIMEOUT_SECONDS;
    @JsonProperty
    private Integer agreementIntervalMilliseconds = DEFAULT_SCHEMA_AGREEMENT_INTERVAL_MILLISECONDS;
    @JsonProperty
    private Boolean agreementWarnOnFailure = DEFAULT_SCHEMA_AGREEMENT_WARN_ON_FAILURE;

    public Integer getAgreementIntervalMilliseconds() {
        return agreementIntervalMilliseconds;
    }

    public void setAgreementIntervalMilliseconds(Integer agreementIntervalMilliseconds) {
        this.agreementIntervalMilliseconds = agreementIntervalMilliseconds;
    }

    public Integer getAgreementTimeoutSeconds() {
        return agreementTimeoutSeconds;
    }

    public void setAgreementTimeoutSeconds(Integer agreementTimeoutSeconds) {
        this.agreementTimeoutSeconds = agreementTimeoutSeconds;
    }

    public Boolean getAgreementWarnOnFailure() {
        return agreementWarnOnFailure;
    }

    public void setAgreementWarnOnFailure(Boolean agreementWarnOnFailure) {
        this.agreementWarnOnFailure = agreementWarnOnFailure;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder
            .withNullSafeInteger(CONTROL_CONNECTION_AGREEMENT_INTERVAL, getAgreementIntervalMilliseconds())
            .withNullSafeInteger(CONTROL_CONNECTION_AGREEMENT_TIMEOUT, getAgreementTimeoutSeconds())
            .withNullSafeBoolean(CONTROL_CONNECTION_AGREEMENT_WARN, getAgreementWarnOnFailure());
    }
}
