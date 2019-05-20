package io.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.ErrorAwarePolicy;
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

public class ErrorAwarePolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<LoadBalancingPolicyFactory> factory =
            new YamlConfigurationFactory<>(LoadBalancingPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(ErrorAwarePolicyFactory.class);
    }

    @Test
    public void shouldBuildAnErrorAwarePolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/loadbalancing/error-aware.yaml").toURI());
        final LoadBalancingPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(ErrorAwarePolicyFactory.class);
        final ErrorAwarePolicyFactory loadBalancingFactory = (ErrorAwarePolicyFactory) factory;
        assertThat(loadBalancingFactory.getSubPolicy()).isInstanceOf(RoundRobinPolicyFactory.class);
        assertThat(loadBalancingFactory.getMaxErrorsPerMinute()).isEqualTo(10);
        assertThat(loadBalancingFactory.getRetryPeriod()).isEqualTo(Duration.seconds(5));

        assertThat(factory.build()).isInstanceOf(ErrorAwarePolicy.class);
    }
}
