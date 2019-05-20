package io.dropwizard.cassandra.netty;

import com.datastax.driver.core.NettyOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class DefaultNettyOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<NettyOptionsFactory> factory =
            new YamlConfigurationFactory<>(NettyOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildNettyOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/netty/default.yaml").toURI());
        final NettyOptionsFactory factory = this.factory.build(yaml);
        Assertions.assertThat(factory).isInstanceOf(DefaultNettyOptionsFactory.class);

        Assertions.assertThat(factory.build()).isInstanceOf(NettyOptions.class);
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(DefaultNettyOptionsFactory.class));
    }
}
