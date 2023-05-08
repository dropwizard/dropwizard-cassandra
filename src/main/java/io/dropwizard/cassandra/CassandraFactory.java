package io.dropwizard.cassandra;

import brave.Tracing;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.cassandra.auth.AuthProviderFactory;
import io.dropwizard.cassandra.health.CassandraHealthCheck;
import io.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory;
import io.dropwizard.cassandra.managed.CassandraManager;
import io.dropwizard.cassandra.network.AddressTranslatorFactory;
import io.dropwizard.cassandra.options.CassandraOption;
import io.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import io.dropwizard.cassandra.protocolVersion.ProtocolVersionFactory;
import io.dropwizard.cassandra.reconnection.ExponentialReconnectionPolicyFactory;
import io.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import io.dropwizard.cassandra.request.RequestOptionsFactory;
import io.dropwizard.cassandra.retry.RetryPolicyFactory;
import io.dropwizard.cassandra.schema.SchemaOptionsFactory;
import io.dropwizard.cassandra.speculativeexecution.SpeculativeExecutionPolicyFactory;
import io.dropwizard.cassandra.ssl.SSLOptionsFactory;
import io.dropwizard.cassandra.timestamp.AtomicMonotonicTimestampGeneratorFactory;
import io.dropwizard.cassandra.timestamp.TimestampGeneratorFactory;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class CassandraFactory implements Discoverable {
    private static final Logger log = LoggerFactory.getLogger(CassandraFactory.class);
    @JsonProperty
    protected String sessionName;
    @JsonProperty
    protected String sessionKeyspaceName;
    @JsonProperty
    protected RequestOptionsFactory requestOptionsFactory;
    @JsonProperty
    protected boolean metricsEnabled = true;
    @NotNull
    @JsonProperty
    protected String validationQuery = "SELECT key FROM system.local;";
    @JsonProperty
    protected ProtocolVersionFactory protocolVersion;
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
    protected SchemaOptionsFactory schemaOptions;
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
    @Valid
    @NotNull
    @JsonProperty
    protected LoadBalancingPolicyFactory loadBalancingPolicy;
    @JsonProperty
    protected List<CassandraOption> cassandraOptions;
    @JsonProperty
    protected List<String> sessionMetrics;

    @JsonProperty
    protected List<String> nodeMetrics;

    public List<String> getSessionMetrics() {
        return sessionMetrics;
    }

    public void setSessionMetrics(List<String> sessionMetrics) {
        this.sessionMetrics = sessionMetrics;
    }

    public List<String> getNodeMetrics() {
        return nodeMetrics;
    }

    public void setNodeMetrics(List<String> nodeMetrics) {
        this.nodeMetrics = nodeMetrics;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionKeyspaceName() {
        return sessionKeyspaceName;
    }

    public void setSessionKeyspaceName(String sessionKeyspaceName) {
        this.sessionKeyspaceName = sessionKeyspaceName;
    }

    public RequestOptionsFactory getRequestOptionsFactory() {
        return requestOptionsFactory;
    }

    public void setRequestOptionsFactory(RequestOptionsFactory requestOptionsFactory) {
        this.requestOptionsFactory = requestOptionsFactory;
    }

    public List<CassandraOption> getCassandraOptions() {
        return cassandraOptions;
    }

    public void setCassandraOptions(List<CassandraOption> cassandraOptions) {
        this.cassandraOptions = cassandraOptions;
    }

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

    public ProtocolVersionFactory getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(final ProtocolVersionFactory protocolVersion) {
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

    public SchemaOptionsFactory getSchemaOptions() {
        return schemaOptions;
    }

    public void setSchemaOptions(final SchemaOptionsFactory schemaOptions) {
        this.schemaOptions = schemaOptions;
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


        configLoaderBuilder.withNullSafeStringList(DefaultDriverOption.METRICS_NODE_ENABLED, nodeMetrics);
        configLoaderBuilder.withNullSafeStringList(DefaultDriverOption.METRICS_SESSION_ENABLED, sessionMetrics);
        configLoaderBuilder.withNullSafeString(DefaultDriverOption.PROTOCOL_COMPRESSION, compression);

        this.configBuilderHelper(ssl, configLoaderBuilder)
                .configBuilderHelper(authProvider, configLoaderBuilder)
                .configBuilderHelper(retryPolicy, configLoaderBuilder)
                .configBuilderHelper(speculativeExecutionPolicy, configLoaderBuilder)
                .configBuilderHelper(poolingOptions, configLoaderBuilder)
                .configBuilderHelper(schemaOptions, configLoaderBuilder)
                .configBuilderHelper(addressTranslator, configLoaderBuilder)
                .configBuilderHelper(requestOptionsFactory, configLoaderBuilder)
                .configBuilderHelper(loadBalancingPolicy, configLoaderBuilder)
                .configBuilderHelper(timestampGenerator, configLoaderBuilder)
                .configBuilderHelper(reconnectionPolicyFactory, configLoaderBuilder)
                .configBuilderHelper(protocolVersion, configLoaderBuilder);

        if (Objects.nonNull(cassandraOptions)) {
            cassandraOptions.forEach(opt -> opt.accept(configLoaderBuilder));
        }

        configLoaderBuilder.withNullSafeString(DefaultDriverOption.SESSION_NAME, getSessionName());
        configLoaderBuilder.withNullSafeString(DefaultDriverOption.SESSION_KEYSPACE, getSessionKeyspaceName());
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

    protected void setUpLifecycle(final CqlSession session, final LifecycleEnvironment lifecycle) {
        lifecycle.manage(new CassandraManager(session, shutdownGracePeriod));
    }

    public abstract CqlSession build(MetricRegistry metrics, LifecycleEnvironment lifecycle, HealthCheckRegistry healthChecks,
                                     Tracing tracing);

    protected CassandraFactory configBuilderHelper(DropwizardCassandraConfigBuilder dropwizardCassandraConfigBuilder,
                                                   DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        if (Objects.nonNull(dropwizardCassandraConfigBuilder)) {
            dropwizardCassandraConfigBuilder.accept(builder);
        }
        return this;
    }
}
