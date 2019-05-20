package io.dropwizard.cassandra.ssl;

import com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions;
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

public class RemoteEndpointAwareJdkSSLOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SSLOptionsFactory> factory =
            new YamlConfigurationFactory<>(SSLOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(RemoteEndpointAwareJdkSSLOptionsFactory.class);
    }

    @Test
    public void shouldBuildJdkSSLOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/ssl/jdk.yaml").toURI());
        final SSLOptionsFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(RemoteEndpointAwareJdkSSLOptionsFactory.class);

        assertThat(factory.build()).isInstanceOf(RemoteEndpointAwareJdkSSLOptions.class);
    }
}
