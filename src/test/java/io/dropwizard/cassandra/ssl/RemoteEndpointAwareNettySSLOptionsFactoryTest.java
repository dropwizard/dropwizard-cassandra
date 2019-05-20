package io.dropwizard.cassandra.ssl;

import com.datastax.driver.core.RemoteEndpointAwareNettySSLOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslProvider;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoteEndpointAwareNettySSLOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SSLOptionsFactory> factory =
            new YamlConfigurationFactory<>(SSLOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(RemoteEndpointAwareNettySSLOptionsFactory.class);
    }

    @Test
    public void shouldBuildNettySSLOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/ssl/netty.yaml").toURI());
        final SSLOptionsFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(RemoteEndpointAwareNettySSLOptionsFactory.class);

        final RemoteEndpointAwareNettySSLOptionsFactory nettyFactory = (RemoteEndpointAwareNettySSLOptionsFactory) factory;
        assertThat(nettyFactory.getProvider()).isEqualTo(SslProvider.JDK);
        assertThat(nettyFactory.getCiphers()).isEqualTo(Collections.singletonList("cipher"));
        assertThat(nettyFactory.getClientAuth()).isEqualTo(ClientAuth.NONE);
        assertThat(nettyFactory.getSessionCacheSize()).isEqualTo(5L);
        assertThat(nettyFactory.getSessionTimeout()).isEqualTo(Duration.seconds(5));

        assertThat(factory.build()).isInstanceOf(RemoteEndpointAwareNettySSLOptions.class);
    }

}
