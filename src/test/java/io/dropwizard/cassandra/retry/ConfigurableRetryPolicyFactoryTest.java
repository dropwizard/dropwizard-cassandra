package io.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.RetryPolicy;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class ConfigurableRetryPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RetryPolicyFactory> factory =
            new YamlConfigurationFactory<>(RetryPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAConfigurableRetryPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/retry/configurable.yaml").toURI());
        final RetryPolicyFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ConfigurableRetryPolicyFactory.class));
        assertThat(((ConfigurableRetryPolicyFactory) factory).getWriteTimeoutRetries(), is(3));
        assertThat(factory.build(), instanceOf(ConfigurableRetryPolicy.class));
    }

    @Test
    public void buildsPolicyWithDefaults() throws Exception {
        final ConfigurableRetryPolicyFactory factory = new ConfigurableRetryPolicyFactory();

        final RetryPolicy policy = factory.build();

        assertThat(policy, is(instanceOf(ConfigurableRetryPolicy.class)));
        ConfigurableRetryPolicy retryPolicy = (ConfigurableRetryPolicy) policy;
        assertThat(retryPolicy.getReadTimeoutRetries(), is(1));
        assertThat(retryPolicy.getUnavailableRetries(), is(1));
        assertThat(retryPolicy.getWriteTimeoutRetries(), is(1));
    }

    @Test
    public void buildsPolicyWithProvidedValues() {
        final ConfigurableRetryPolicyFactory factory = new ConfigurableRetryPolicyFactory();
        factory.setReadTimeoutRetries(3);
        factory.setWriteTimeoutRetries(3);
        factory.setUnavailableRetries(3);

        final RetryPolicy policy = factory.build();

        assertThat(policy, is(instanceOf(ConfigurableRetryPolicy.class)));

        ConfigurableRetryPolicy retryPolicy = (ConfigurableRetryPolicy) policy;
        assertThat(retryPolicy.getWriteTimeoutRetries(), is(3));
        assertThat(retryPolicy.getReadTimeoutRetries(), is(3));
        assertThat(retryPolicy.getUnavailableRetries(), is(3));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ConfigurableRetryPolicyFactory.class));
    }
}
