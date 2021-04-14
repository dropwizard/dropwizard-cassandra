package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.specex.ConstantSpeculativeExecutionPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
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
import static org.junit.Assert.assertEquals;

public class ConstantSpeculativeExecutionPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SpeculativeExecutionPolicyFactory> factory =
            new YamlConfigurationFactory<>(SpeculativeExecutionPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(ConstantSpeculativeExecutionPolicyFactory.class);
    }

    @Test
    public void shouldBuildConstantSpeculativeExecutionPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/speculativeexecution/constant.yaml").toURI());
        final SpeculativeExecutionPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(ConstantSpeculativeExecutionPolicyFactory.class);
        final ConstantSpeculativeExecutionPolicyFactory speculativeExecutionPolicyFactory =
                (ConstantSpeculativeExecutionPolicyFactory) factory;
        assertThat(speculativeExecutionPolicyFactory.getDelay()).isEqualTo(Duration.seconds(1));
        assertThat(speculativeExecutionPolicyFactory.getMaxSpeculativeExecutions()).isEqualTo(3);

        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile().getString(DefaultDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS),
                ConstantSpeculativeExecutionPolicy.class.getName());
        assertEquals(builder.build().getInitialConfig().getDefaultProfile().getInt(DefaultDriverOption.SPECULATIVE_EXECUTION_MAX),
                3);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile().getInt(DefaultDriverOption.SPECULATIVE_EXECUTION_DELAY),
                1000);
    }
}
