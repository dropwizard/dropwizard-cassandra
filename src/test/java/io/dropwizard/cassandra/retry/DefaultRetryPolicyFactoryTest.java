package io.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
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

public class DefaultRetryPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RetryPolicyFactory> factory =
            new YamlConfigurationFactory<>(RetryPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultRetryPolicyFactory.class);
    }

    @Test
    public void shouldBuildDefaultRetryPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/retry/default.yaml").toURI());
        final RetryPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(DefaultRetryPolicyFactory.class);

        assertThat(factory.build()).isInstanceOf(DefaultRetryPolicy.class);
    }
}
