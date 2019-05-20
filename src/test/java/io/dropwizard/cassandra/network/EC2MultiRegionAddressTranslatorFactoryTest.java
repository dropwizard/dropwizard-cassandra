package io.dropwizard.cassandra.network;

import com.datastax.driver.core.policies.EC2MultiRegionAddressTranslator;
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

public class EC2MultiRegionAddressTranslatorFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<AddressTranslatorFactory> factory =
            new YamlConfigurationFactory<>(AddressTranslatorFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(EC2MultiRegionAddressTranslatorFactory.class);
    }

    @Test
    public void shouldBuildEC2MultiRegionAddressTranslator() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/network/ec2.yaml").toURI());
        final AddressTranslatorFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(EC2MultiRegionAddressTranslatorFactory.class);

        assertThat(factory.build()).isInstanceOf(EC2MultiRegionAddressTranslator.class);
    }
}
