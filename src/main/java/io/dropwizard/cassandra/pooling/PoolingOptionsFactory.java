package io.dropwizard.cassandra.pooling;

import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

import javax.validation.Valid;

public class PoolingOptionsFactory {
    @Valid
    @JsonProperty
    private Duration heartbeatInterval;
    @Valid
    @JsonProperty
    private Duration poolTimeout;
    @Valid
    @JsonProperty
    private Duration idleTimeout;
    @Valid
    @JsonProperty
    private HostDistanceOptions remote;
    @Valid
    @JsonProperty
    private HostDistanceOptions local;

    public Duration getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Duration heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Duration getPoolTimeout() {
        return poolTimeout;
    }

    public void setPoolTimeout(Duration poolTimeout) {
        this.poolTimeout = poolTimeout;
    }

    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Duration idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public HostDistanceOptions getRemote() {
        return remote;
    }

    public void setRemote(HostDistanceOptions remote) {
        this.remote = remote;
    }

    public HostDistanceOptions getLocal() {
        return local;
    }

    public void setLocal(HostDistanceOptions local) {
        this.local = local;
    }

    public PoolingOptions build() {
        final PoolingOptions poolingOptions = new PoolingOptions();
        if (local != null) {
            setPoolingOptions(poolingOptions, HostDistance.LOCAL, local);
        }
        if (remote != null) {
            setPoolingOptions(poolingOptions, HostDistance.REMOTE, remote);
        }
        if (heartbeatInterval != null) {
            poolingOptions.setHeartbeatIntervalSeconds((int) heartbeatInterval.toSeconds());
        }
        if (poolTimeout != null) {
            poolingOptions.setPoolTimeoutMillis((int) poolTimeout.toMilliseconds());
        }
        if (idleTimeout != null) {
            poolingOptions.setIdleTimeoutSeconds((int) idleTimeout.toSeconds());
        }
        return poolingOptions;
    }

    private void setPoolingOptions(final PoolingOptions poolingOptions, final HostDistance hostDistance, final HostDistanceOptions options) {
        if (options.getCoreConnections() != null) {
            poolingOptions.setCoreConnectionsPerHost(hostDistance, options.getCoreConnections());
        }
        if (options.getMaxConnections() != null) {
            poolingOptions.setMaxConnectionsPerHost(hostDistance, options.getMaxConnections());
        }
        if (options.getMaxRequestsPerConnection() != null) {
            poolingOptions.setMaxRequestsPerConnection(hostDistance, options.getMaxRequestsPerConnection());
        }
        if (options.getNewConnectionThreshold() != null) {
            poolingOptions.setNewConnectionThreshold(hostDistance, options.getNewConnectionThreshold());
        }
    }

}
