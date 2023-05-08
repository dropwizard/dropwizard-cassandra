package io.dropwizard.cassandra;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.PortRange;

import jakarta.validation.constraints.NotNull;

public class ContactPoint {
    @NotNull
    @JsonProperty
    private String host;
    @NotNull
    @JsonProperty
    @PortRange
    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
