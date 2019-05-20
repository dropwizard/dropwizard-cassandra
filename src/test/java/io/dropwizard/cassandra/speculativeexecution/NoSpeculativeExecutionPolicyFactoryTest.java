package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.NoSpeculativeExecutionPolicy;
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

public class NoSpeculativeExecutionPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SpeculativeExecutionPolicyFactory> factory =
            new YamlConfigurationFactory<>(SpeculativeExecutionPolicyFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(NoSpeculativeExecutionPolicyFactory.class);
    }

    @Test
    public void shouldBuildNoSpeculativeExecutionPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/speculativeexecution/no.yaml").toURI());
        final SpeculativeExecutionPolicyFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(NoSpeculativeExecutionPolicyFactory.class);

        assertThat(factory.build()).isInstanceOf(NoSpeculativeExecutionPolicy.class);
    }
}
