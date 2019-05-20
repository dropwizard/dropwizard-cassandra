package io.dropwizard.cassandra.test.smoke;

import io.dropwizard.cassandra.CassandraFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SmokeTestConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private CassandraFactory cassandra;

    public CassandraFactory getCassandraFactory() {
        return cassandra;
    }

    public void setCassandraFactory(final CassandraFactory cassandraConfig) {
        this.cassandra = cassandraConfig;
    }
}
