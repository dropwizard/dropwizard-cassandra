package io.dropwizard.cassandra.pooling;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class HostDistanceOptions {
    @Min(0)
    @JsonProperty
    private Integer maxRequestsPerConnection;
    @Min(0)
    @JsonProperty
    private Integer newConnectionThreshold;
    @Min(0)
    @JsonProperty
    private Integer coreConnections;
    @Min(0)
    @JsonProperty
    private Integer maxConnections;

    public Integer getMaxRequestsPerConnection() {
        return maxRequestsPerConnection;
    }

    public void setMaxRequestsPerConnection(Integer maxRequestsPerConnection) {
        this.maxRequestsPerConnection = maxRequestsPerConnection;
    }

    public Integer getNewConnectionThreshold() {
        return newConnectionThreshold;
    }

    public void setNewConnectionThreshold(Integer newConnectionThreshold) {
        this.newConnectionThreshold = newConnectionThreshold;
    }

    public Integer getCoreConnections() {
        return coreConnections;
    }

    public void setCoreConnections(Integer coreConnections) {
        this.coreConnections = coreConnections;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }
}
