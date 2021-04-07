package io.dropwizard.cassandra;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.auth.AuthProviderFactory;
import io.dropwizard.cassandra.health.CassandraHealthCheck;
import io.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory;
import io.dropwizard.cassandra.managed.CassandraManager;
import io.dropwizard.cassandra.metrics.CassandraMetricRegistryListener;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class CassandraFactory implements Discoverable {
    private static final Logger log = LoggerFactory.getLogger(CassandraFactory.class);
    @JsonProperty
    protected boolean metricsEnabled = true;
    @NotNull
    @JsonProperty
    protected String validationQuery = "SELECT key FROM system.local;";
    @NotNull
    @JsonProperty
    protected ProtocolVersion protocolVersion = ProtocolVersion.DEFAULT;
    @Valid
    @JsonProperty
    protected SSLOptionsFactory ssl;
    @NotNull
    @JsonProperty
    protected String compression = "lz4";
    @Valid
    @JsonProperty
    protected AuthProviderFactory authProvider;
    @Valid
    @JsonProperty
    protected RetryPolicyFactory retryPolicy;
    @Valid
    @JsonProperty
    protected SpeculativeExecutionPolicyFactory speculativeExecutionPolicy;
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
    @NotNull
    @Valid
    @JsonProperty
    protected ReconnectionPolicyFactory reconnectionPolicyFactory = new ExponentialReconnectionPolicyFactory();
    @MaxDuration(value = Integer.MAX_VALUE)
    @Valid
    @NotNull
    @JsonProperty
    protected LoadBalancingPolicyFactory loadBalancingPolicy;

    @NotNull
    @JsonProperty
    protected Double reconnectionThreshold = 0.5d;

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public void setMetricsEnabled(final boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
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

    public String getCompression() {
        return compression;
    }

    public void setCompression(final String compression) {
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

    public ReconnectionPolicyFactory getReconnectionPolicyFactory() {
        return reconnectionPolicyFactory;
    }

    public void setReconnectionPolicyFactory(final ReconnectionPolicyFactory reconnectionPolicyFactory) {
        this.reconnectionPolicyFactory = reconnectionPolicyFactory;
    }

    public LoadBalancingPolicyFactory getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(final LoadBalancingPolicyFactory loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public Double getReconnectionThreshold() {
        return reconnectionThreshold;
    }

    public void setReconnectionThreshold(final Double reconnectionThreshold) {
        this.reconnectionThreshold = reconnectionThreshold;
    }

    protected CqlSessionBuilder setUpClusterBuilder(final MetricRegistry metrics) {
        CqlSessionBuilder builder = CqlSession.builder();
        if (metricsEnabled) {
            builder.withMetricRegistry(metrics);
        }
        return builder.withConfigLoader(getConfig(metrics));
    }

    protected DriverConfigLoader getConfig(final MetricRegistry metrics) {

        DropwizardProgrammaticDriverConfigLoaderBuilder configLoaderBuilder =
                DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

        configLoaderBuilder.withNullSafeString(DefaultDriverOption.PROTOCOL_VERSION, protocolVersion.name())
                .withNullSafeString(DefaultDriverOption.PROTOCOL_COMPRESSION, compression);


        this.configBuilderHelper(ssl, configLoaderBuilder)
                .configBuilderHelper(authProvider, configLoaderBuilder)
                .configBuilderHelper(retryPolicy, configLoaderBuilder)
                .configBuilderHelper(speculativeExecutionPolicy, configLoaderBuilder)
                .configBuilderHelper(poolingOptions, configLoaderBuilder)
                .configBuilderHelper(addressTranslator, configLoaderBuilder);

        addAdditionalBuilderOptions(configLoaderBuilder, metrics);
        return configLoaderBuilder.build();
    }

    /**
     * Point of extension, if any additional builder options that are not provided by default are desired
     *
     * @param loader  the cluster builder
     * @param metrics dropwizard app metric registry
     */
    protected void addAdditionalBuilderOptions(final ProgrammaticDriverConfigLoaderBuilder loader,
                                               final MetricRegistry metrics) {
        // does nothing by default
    }

    protected void setUpHealthChecks(final CqlSession session, final HealthCheckRegistry healthChecks) {
        log.debug("Registering Cassandra health check for name={}", session.getName());
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(session, getValidationQuery(), getHealthCheckTimeout());
        healthChecks.register(session.getName(), healthCheck);
    }

    protected void setUpMetrics(final CqlSession session, final MetricRegistry metrics) {
        // ties the Cassandra driver metrics into the app's MetricRegistry
        if (metricsEnabled) {
            final CassandraMetricRegistryListener metricRegistryListener = new CassandraMetricRegistryListener(metrics, session.getName());
            session.getMetrics().ifPresent(metricsRegistry -> metricsRegistry.getRegistry().addListener(metricRegistryListener));
        }
    }

    protected void setUpLifecycle(final CqlSession session, final LifecycleEnvironment lifecycle) {
        lifecycle.manage(new CassandraManager(session, shutdownGracePeriod));
    }

    public abstract CqlSession build(MetricRegistry metrics, LifecycleEnvironment lifecycle, HealthCheckRegistry healthChecks,
                                     Tracing tracing);

    protected CassandraFactory configBuilderHelper(DropwizardCassandraConfigBuilder dropwizardCassandraConfigBuilder,
                                                   DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        if (Objects.nonNull(dropwizardCassandraConfigBuilder)) {
            dropwizardCassandraConfigBuilder.build(builder);
        }
        return this;
    }
}
