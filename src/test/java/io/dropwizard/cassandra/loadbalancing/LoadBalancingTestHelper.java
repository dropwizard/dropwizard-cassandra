package io.dropwizard.cassandra.loadbalancing;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.api.core.loadbalancing.LoadBalancingPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class LoadBalancingTestHelper<T extends BasicPolicyFactory> {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<LoadBalancingPolicyFactory> factory =
            new YamlConfigurationFactory<>(LoadBalancingPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(getPolicyFactoryClass());
    }

    @Test
    public void shouldBuildADCAwareRoundRobinPolicy() throws Exception {
        final File yaml = new File(Resources.getResource(getYaml()).toURI());
        final LoadBalancingPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(getPolicyFactoryClass());
        final T loadBalancingFactory = (T) factory;

        assertThat(loadBalancingFactory.getLocalDataCenter()).isEqualTo("local");
        assertThat(loadBalancingFactory.getDataCenterFailoverAllowLocalConsistencyLevels()).isEqualTo(true);
        assertThat(loadBalancingFactory.getSlowAvoidance()).isEqualTo(true);
        assertThat(loadBalancingFactory.getDcFailoverMaxNodesPerRemoteDc()).isEqualTo(2);

        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        loadBalancingFactory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        assertThat(profile.getString(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS)).isEqualTo(getLoadBalancingPolicy().getName());
        assertThat(profile.getString(DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER)).isEqualTo("local");
        assertThat(profile.getBoolean(DefaultDriverOption.LOAD_BALANCING_DC_FAILOVER_ALLOW_FOR_LOCAL_CONSISTENCY_LEVELS))
                .isEqualTo(true);
        assertThat(profile.getBoolean(DefaultDriverOption.LOAD_BALANCING_POLICY_SLOW_AVOIDANCE))
                .isEqualTo(true);
        assertThat(profile.getInt(DefaultDriverOption.LOAD_BALANCING_DC_FAILOVER_MAX_NODES_PER_REMOTE_DC))
                .isEqualTo(2);
    }

    public abstract Class<T> getPolicyFactoryClass();

    public abstract String getYaml();

    public abstract Class<? extends LoadBalancingPolicy> getLoadBalancingPolicy();
}

