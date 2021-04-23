package io.dropwizard.cassandra.reconnection;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.connection.ConstantReconnectionPolicy;
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
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ConstantReconnectionPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ReconnectionPolicyFactory> factory =
            new YamlConfigurationFactory<>(ReconnectionPolicyFactory.class, validator, objectMapper, "dw");
    private final DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

    @Test
    public void shouldBuildAConstantReconnectionPolicyFactory() throws URISyntaxException, IOException, ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/reconnection/constant.yaml").toURI());
        final ReconnectionPolicyFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ConstantReconnectionPolicyFactory.class));
        assertThat(((ConstantReconnectionPolicyFactory) factory).getDelay(), is(Duration.minutes(1)));
        factory.accept(builder);
        Assertions.assertThat(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.RECONNECTION_POLICY_CLASS))
                .isEqualTo(ConstantReconnectionPolicy.class.getName());
    }

    @Test
    public void buildsPolicyWithDefaults() {
        final ConstantReconnectionPolicyFactory factory = new ConstantReconnectionPolicyFactory();

        factory.accept(builder);
        Assertions.assertThat(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.RECONNECTION_POLICY_CLASS))
                .isEqualTo(ConstantReconnectionPolicy.class.getName());
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ConstantReconnectionPolicyFactory.class));
    }
}
