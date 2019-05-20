package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class DCAwareRoundRobinPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<LoadBalancingPolicyFactory> factory =
            new YamlConfigurationFactory<>(LoadBalancingPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DCAwareRoundRobinPolicyFactory.class);
    }

    @Test
    public void shouldBuildADCAwareRoundRobinPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/loadbalancing/dc-aware.yaml").toURI());
        final LoadBalancingPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(DCAwareRoundRobinPolicyFactory.class);
        final DCAwareRoundRobinPolicyFactory loadBalancingFactory = (DCAwareRoundRobinPolicyFactory) factory;
        assertThat(loadBalancingFactory.getLocalDC()).isEqualTo("DC1");

        assertThat(factory.build()).isInstanceOf(DCAwareRoundRobinPolicy.class);
    }
}
