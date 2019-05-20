package io.dropwizard.cassandra.pooling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class HostDistanceOptionsTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<HostDistanceOptions> factory =
            new YamlConfigurationFactory<>(HostDistanceOptions.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildHostOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/pooling/host-distance.yaml").toURI());
        final HostDistanceOptions factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(HostDistanceOptions.class);
        assertThat(factory.getMaxRequestsPerConnection()).isEqualTo(5);
        assertThat(factory.getNewConnectionThreshold()).isEqualTo(2);
        assertThat(factory.getCoreConnections()).isEqualTo(10);
        assertThat(factory.getMaxConnections()).isEqualTo(10);
    }
}
