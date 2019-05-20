package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class LatencyAwarePolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<LoadBalancingPolicyFactory> factory =
            new YamlConfigurationFactory<>(LoadBalancingPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(LatencyAwarePolicyFactory.class);
    }

    @Test
    public void shouldBuildALatencyAwarePolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/loadbalancing/latency-aware.yaml").toURI());
        final LoadBalancingPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(LatencyAwarePolicyFactory.class);
        final LatencyAwarePolicyFactory loadBalancingFactory = (LatencyAwarePolicyFactory) factory;
        assertThat(loadBalancingFactory.getSubPolicy()).isInstanceOf(RoundRobinPolicyFactory.class);
        assertThat(loadBalancingFactory.getExclusionThreshold()).isEqualTo(1.5d);
        assertThat(loadBalancingFactory.getMinimumMeasurements()).isEqualTo(1);
        assertThat(loadBalancingFactory.getRetryPeriod()).isEqualTo(Duration.seconds(5));
        assertThat(loadBalancingFactory.getScale()).isEqualTo(Duration.seconds(1));
        assertThat(loadBalancingFactory.getUpdateRate()).isEqualTo(Duration.seconds(1));

        assertThat(factory.build()).isInstanceOf(LatencyAwarePolicy.class);
    }
}
