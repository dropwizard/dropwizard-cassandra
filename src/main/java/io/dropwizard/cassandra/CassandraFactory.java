package io.dropwizard.cassandra;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.auth.AuthProviderFactory;
import io.dropwizard.cassandra.health.CassandraHealthCheck;
import io.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory;
import io.dropwizard.cassandra.managed.CassandraManager;
import io.dropwizard.cassandra.metrics.CassandraMetricRegistryListener;
import io.dropwizard.cassandra.netty.NettyOptionsFactory;
import io.dropwizard.cassandra.network.AddressTranslatorFactory;
import io.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import io.dropwizard.cassandra.reconnection.ExponentialReconnectionPolicyFactory;
import io.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import io.dropwizard.cassandra.retry.RetryPolicyFactory;
import io.dropwizard.cassandra.speculativeexecution.SpeculativeExecutionPolicyFactory;
import io.dropwizard.cassandra.ssl.SSLOptionsFactory;
import io.dropwizard.cassandra.timestamp.AtomicMonotonicTimestampGeneratorFactory;
import io.dropwizard.cassandra.timestamp.TimestampGeneratorFactory;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;
import io.dropwizard.validation.PortRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class CassandraFactory implements Discoverable {
    private static final Logger log = LoggerFactory.getLogger(CassandraFactory.class);

    @NotNull
    @JsonProperty
    protected String name = "cassandra";
    @JsonProperty
    protected boolean metricsEnabled = true;
    @JsonProperty
    private boolean jmxEnabled = false;
    @NotNull
    @JsonProperty
    protected String connectionString;
    @NotNull
    @JsonProperty
    protected String clusterName;
    @NotNull
    @JsonProperty
    protected String validationQuery = "SELECT key FROM system.local;";
    @NotNull
    @JsonProperty
    protected ProtocolVersion protocolVersion = ProtocolVersion.NEWEST_SUPPORTED;
    @Valid
    @JsonProperty
    protected SSLOptionsFactory ssl;
    @NotNull
    @JsonProperty
    protected ProtocolOptions.Compression compression = ProtocolOptions.Compression.LZ4;
    @Valid
    @JsonProperty
    protected AuthProviderFactory authProvider;
    @Valid
    @JsonProperty
    protected RetryPolicyFactory retryPolicy;
    @Valid
    @JsonProperty
    protected SpeculativeExecutionPolicyFactory speculativeExecutionPolicy;
    @JsonProperty
    protected QueryOptions queryOptions;
    @JsonProperty
    protected SocketOptions socketOptions;
    @Valid
    @JsonProperty
    protected PoolingOptionsFactory poolingOptions;
    @Valid
    @JsonProperty
    protected AddressTranslatorFactory addressTranslator;
    @NotNull
    @JsonProperty
    protected Duration shutdownGracePeriod = Duration.seconds(10);
    @NotNull
    @JsonProperty
    protected Duration healthCheckTimeout = Duration.seconds(5);
    @Valid
    @JsonProperty
    protected TimestampGeneratorFactory timestampGenerator = new AtomicMonotonicTimestampGeneratorFactory();
    @Valid
    @JsonProperty
    protected NettyOptionsFactory nettyOptions;
    @NotNull
    @Valid
    @JsonProperty
    protected ReconnectionPolicyFactory reconnectionPolicyFactory = new ExponentialReconnectionPolicyFactory();
    @MaxDuration(value = Integer.MAX_VALUE)
    @NotNull
    @JsonProperty
    protected Duration maxSchemaAgreementWait = Duration.seconds(ProtocolOptions.DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS);
    @Valid
    @NotNull
    @JsonProperty
    protected LoadBalancingPolicyFactory loadBalancingPolicy;

    @PortRange
    @JsonProperty
    protected int port = 9042;

    @NotNull
    @JsonProperty
    protected Double reconnectionThreshold = 0.5d;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public void setMetricsEnabled(final boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(final boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(final String connectionString) {
        this.connectionString = connectionString;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(final String clusterName) {
        this.clusterName = clusterName;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(final String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(final ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public SSLOptionsFactory getSsl() {
        return ssl;
    }

    public void setSsl(final SSLOptionsFactory ssl) {
        this.ssl = ssl;
    }

    public ProtocolOptions.Compression getCompression() {
        return compression;
    }

    public void setCompression(final ProtocolOptions.Compression compression) {
        this.compression = compression;
    }

    public AuthProviderFactory getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(final AuthProviderFactory authProvider) {
        this.authProvider = authProvider;
    }

    public RetryPolicyFactory getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(final RetryPolicyFactory retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public SpeculativeExecutionPolicyFactory getSpeculativeExecutionPolicy() {
        return speculativeExecutionPolicy;
    }

    public void setSpeculativeExecutionPolicy(final SpeculativeExecutionPolicyFactory speculativeExecutionPolicy) {
        this.speculativeExecutionPolicy = speculativeExecutionPolicy;
    }

    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    public void setQueryOptions(final QueryOptions queryOptions) {
        this.queryOptions = queryOptions;
    }

    public SocketOptions getSocketOptions() {
        return socketOptions;
    }

    public void setSocketOptions(final SocketOptions socketOptions) {
        this.socketOptions = socketOptions;
    }

    public PoolingOptionsFactory getPoolingOptions() {
        return poolingOptions;
    }

    public void setPoolingOptions(final PoolingOptionsFactory poolingOptions) {
        this.poolingOptions = poolingOptions;
    }

    public AddressTranslatorFactory getAddressTranslator() {
        return addressTranslator;
    }

    public void setAddressTranslator(final AddressTranslatorFactory addressTranslator) {
        this.addressTranslator = addressTranslator;
    }

    public Duration getShutdownGracePeriod() {
        return shutdownGracePeriod;
    }

    public void setShutdownGracePeriod(final Duration shutdownGracePeriod) {
        this.shutdownGracePeriod = shutdownGracePeriod;
    }

    public Duration getHealthCheckTimeout() {
        return healthCheckTimeout;
    }

    public void setHealthCheckTimeout(final Duration healthCheckTimeout) {
        this.healthCheckTimeout = healthCheckTimeout;
    }

    public TimestampGeneratorFactory getTimestampGenerator() {
        return timestampGenerator;
    }

    public void setTimestampGenerator(final TimestampGeneratorFactory timestampGenerator) {
        this.timestampGenerator = timestampGenerator;
    }

    public NettyOptionsFactory getNettyOptions() {
        return nettyOptions;
    }

    public void setNettyOptions(final NettyOptionsFactory nettyOptions) {
        this.nettyOptions = nettyOptions;
    }

    public ReconnectionPolicyFactory getReconnectionPolicyFactory() {
        return reconnectionPolicyFactory;
    }

    public void setReconnectionPolicyFactory(final ReconnectionPolicyFactory reconnectionPolicyFactory) {
        this.reconnectionPolicyFactory = reconnectionPolicyFactory;
    }

    public Duration getMaxSchemaAgreementWait() {
        return maxSchemaAgreementWait;
    }

    public void setMaxSchemaAgreementWait(final Duration maxSchemaAgreementWait) {
        this.maxSchemaAgreementWait = maxSchemaAgreementWait;
    }

    public LoadBalancingPolicyFactory getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(final LoadBalancingPolicyFactory loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public Double getReconnectionThreshold() {
        return reconnectionThreshold;
    }

    public void setReconnectionThreshold(final Double reconnectionThreshold) {
        this.reconnectionThreshold = reconnectionThreshold;
    }

    protected Cluster.Builder setUpClusterBuilder(final MetricRegistry metrics) {
        final Cluster.Builder builder = Cluster.builder()
                .withCompression(compression)
                .withTimestampGenerator(timestampGenerator.build())
                .withReconnectionPolicy(reconnectionPolicyFactory.build())
                .withClusterName(clusterName)
                .withLoadBalancingPolicy(loadBalancingPolicy.build())
                .withProtocolVersion(protocolVersion);

        if (!jmxEnabled) {
            builder.withoutJMXReporting();
        }

        if (!metricsEnabled) {
            builder.withoutMetrics();
        }

        if (ssl != null) {
            builder.withSSL(ssl.build());
        }

        if (authProvider != null) {
            builder.withAuthProvider(authProvider.build());
        }

        if (retryPolicy != null) {
            builder.withRetryPolicy(retryPolicy.build());
        }

        if (speculativeExecutionPolicy != null) {
            builder.withSpeculativeExecutionPolicy(speculativeExecutionPolicy.build());
        }

        if (queryOptions != null) {
            builder.withQueryOptions(queryOptions);
        }

        if (socketOptions != null) {
            builder.withSocketOptions(socketOptions);
        }

        if (poolingOptions != null) {
            builder.withPoolingOptions(poolingOptions.build());
        }

        if (nettyOptions != null) {
            builder.withNettyOptions(nettyOptions.build());
        }

        if (addressTranslator != null) {
            builder.withAddressTranslator(addressTranslator.build());
        }

        addAdditionalBuilderOptions(builder, metrics);

        return builder;
    }

    /**
     * Point of extension, if any additional builder options that are not provided by default are desired
     *
     * @param builder the cluster builder
     * @param metrics dropwizard app metric registry
     */
    protected void addAdditionalBuilderOptions(final Cluster.Builder builder,
                                               final MetricRegistry metrics) {
        // does nothing by default
    }

    protected void setUpHealthChecks(final Session session, final HealthCheckRegistry healthChecks) {
        log.debug("Registering Cassandra health check for name={}", name);
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(session, getValidationQuery(), getHealthCheckTimeout());
        healthChecks.register(name, healthCheck);
    }

    protected void setUpMetrics(final Cluster cluster, final MetricRegistry metrics) {
        // ties the Cassandra driver metrics into the app's MetricRegistry
        if (metricsEnabled) {
            final CassandraMetricRegistryListener metricRegistryListener = new CassandraMetricRegistryListener(metrics, name);
            cluster.getMetrics().getRegistry().addListener(metricRegistryListener);
        }
    }

    protected void setUpLifecycle(final Cluster cluster, final LifecycleEnvironment lifecycle) {
        lifecycle.manage(new CassandraManager(cluster, shutdownGracePeriod));
    }

    public abstract Session build(MetricRegistry metrics, LifecycleEnvironment lifecycle, HealthCheckRegistry healthChecks,
                                  Tracing tracing);
}
