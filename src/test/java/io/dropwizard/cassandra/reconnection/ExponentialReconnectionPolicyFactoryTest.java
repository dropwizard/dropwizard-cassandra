package io.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class ExponentialReconnectionPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ReconnectionPolicyFactory> factory =
            new YamlConfigurationFactory<>(ReconnectionPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAExponentialReconnectionPolicyFactory() throws URISyntaxException, IOException, ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/reconnection/exponential.yaml").toURI());
        final ReconnectionPolicyFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ExponentialReconnectionPolicyFactory.class));
        assertThat(((ExponentialReconnectionPolicyFactory) factory).getBaseConnectionDelay(), is(Duration.seconds(10)));
        assertThat(((ExponentialReconnectionPolicyFactory) factory).getMaxReconnectionDelay(), is(Duration.seconds(30)));
        assertThat(factory.build(), instanceOf(ExponentialReconnectionPolicy.class));
    }

    @Test
    public void buildsPolicyWithDefaults() {
        final ExponentialReconnectionPolicyFactory factory = new ExponentialReconnectionPolicyFactory();

        assertThat(factory.build(), is(instanceOf(ExponentialReconnectionPolicy.class)));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ExponentialReconnectionPolicyFactory.class));
    }
}
