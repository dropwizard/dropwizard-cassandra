package io.dropwizard.cassandra.network;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.addresstranslation.Ec2MultiRegionAddressTranslator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
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
    DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

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
        factory.accept(builder);
        assertThat(builder.build().getInitialConfig().getDefaultProfile()
                .getString(DefaultDriverOption.ADDRESS_TRANSLATOR_CLASS))
                .isEqualTo(Ec2MultiRegionAddressTranslator.class.getName());
    }
}
