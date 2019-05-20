package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.WhiteListPolicy;
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
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class WhiteListPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<LoadBalancingPolicyFactory> factory =
            new YamlConfigurationFactory<>(LoadBalancingPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(WhiteListPolicyFactory.class);
    }

    @Test
    public void shouldBuildAWhiteListPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/loadbalancing/whitelist.yaml").toURI());
        final LoadBalancingPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(WhiteListPolicyFactory.class);
        final WhiteListPolicyFactory loadBalancingFactory = (WhiteListPolicyFactory) factory;
        assertThat(loadBalancingFactory.getSubPolicy()).isInstanceOf(RoundRobinPolicyFactory.class);
        assertThat(loadBalancingFactory.getWhiteList())
                .contains(new InetSocketAddress("127.0.0.1", 9042));

        assertThat(factory.build()).isInstanceOf(WhiteListPolicy.class);
    }
}
