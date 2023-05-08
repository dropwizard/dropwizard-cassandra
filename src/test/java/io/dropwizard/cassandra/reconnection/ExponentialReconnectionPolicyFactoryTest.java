package io.dropwizard.cassandra.reconnection;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.connection.ExponentialReconnectionPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import jakarta.validation.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class ExponentialReconnectionPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ReconnectionPolicyFactory> factory =
            new YamlConfigurationFactory<>(ReconnectionPolicyFactory.class, validator, objectMapper, "dw");
    private final DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

    @Test
    void shouldBuildAExponentialReconnectionPolicyFactory() throws URISyntaxException, IOException, ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/reconnection/exponential.yaml").toURI());
        final ReconnectionPolicyFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ExponentialReconnectionPolicyFactory.class));
        assertThat(((ExponentialReconnectionPolicyFactory) factory).getBaseConnectionDelay(), is(Duration.seconds(10)));
        assertThat(((ExponentialReconnectionPolicyFactory) factory).getMaxReconnectionDelay(), is(Duration.seconds(30)));
        factory.accept(builder);
        Assertions.assertThat(builder.build().getInitialConfig().getDefaultProfile()
                .getString(DefaultDriverOption.RECONNECTION_POLICY_CLASS))
                .isEqualTo(ExponentialReconnectionPolicy.class.getName());
    }

    @Test
    void buildsPolicyWithDefaults() {
        final ExponentialReconnectionPolicyFactory factory = new ExponentialReconnectionPolicyFactory();
        factory.accept(builder);
        Assertions.assertThat(builder.build().getInitialConfig().getDefaultProfile()
                .getString(DefaultDriverOption.RECONNECTION_POLICY_CLASS))
                .isEqualTo(ExponentialReconnectionPolicy.class.getName());
    }

    @Test
    void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ExponentialReconnectionPolicyFactory.class));
    }
}
