package io.dropwizard.cassandra.pooling;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.cassandra.DropwizardCassandraConfigBuilder;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.util.Duration;

import javax.validation.Valid;

public class PoolingOptionsFactory implements DropwizardCassandraConfigBuilder {
    @JsonProperty
    private Integer maxRequestsPerConnection;
    @JsonProperty
    private Integer maxRemoteConnections;
    @JsonProperty
    private Integer maxLocalConnections;
    @Valid
    @JsonProperty
    private Duration heartbeatInterval;
    @Valid
    @JsonProperty
    private Duration connectionConnectTimeout;

    public Integer getMaxRequestsPerConnection() {
        return maxRequestsPerConnection;
    }

    public void setMaxRequestsPerConnection(Integer maxRequestsPerConnection) {
        this.maxRequestsPerConnection = maxRequestsPerConnection;
    }

    public Integer getMaxRemoteConnections() {
        return maxRemoteConnections;
    }

    public void setMaxRemoteConnections(Integer maxRemoteConnections) {
        this.maxRemoteConnections = maxRemoteConnections;
    }

    public Integer getMaxLocalConnections() {
        return maxLocalConnections;
    }

    public void setMaxLocalConnections(Integer maxLocalConnections) {
        this.maxLocalConnections = maxLocalConnections;
    }

    public Duration getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Duration heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Duration getConnectionConnectTimeout() {
        return connectionConnectTimeout;
    }

    public void setConnectionConnectTimeout(Duration connectionConnectTimeout) {
        this.connectionConnectTimeout = connectionConnectTimeout;
    }

    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withNullSafeInteger(DefaultDriverOption.CONNECTION_MAX_REQUESTS, maxRequestsPerConnection)
                .withNullSafeInteger(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, maxLocalConnections)
                .withNullSafeInteger(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, maxRemoteConnections)
                .withNullSafeDuration(DefaultDriverOption.HEARTBEAT_INTERVAL, heartbeatInterval)
                .withNullSafeDuration(DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT, connectionConnectTimeout);
    }
}
