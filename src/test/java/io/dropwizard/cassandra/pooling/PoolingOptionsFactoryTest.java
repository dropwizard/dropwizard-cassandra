package io.dropwizard.cassandra.pooling;

import com.datastax.driver.core.PoolingOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class PoolingOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<PoolingOptionsFactory> factory =
            new YamlConfigurationFactory<>(PoolingOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildPoolingOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/pooling/pooling-options.yaml").toURI());
        final PoolingOptionsFactory factory = this.factory.build(yaml);
        assertThat(factory.getHeartbeatInterval()).isEqualTo(Duration.seconds(5));
        assertThat(factory.getPoolTimeout()).isEqualTo(Duration.seconds(10));
        assertThat(factory.getIdleTimeout()).isEqualTo(Duration.minutes(1));
        assertThat(factory.getLocal()).isInstanceOf(HostDistanceOptions.class);
        assertThat(factory.getRemote()).isInstanceOf(HostDistanceOptions.class);

        assertThat(factory.build()).isInstanceOf(PoolingOptions.class);
    }
}
