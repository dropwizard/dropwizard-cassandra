package io.dropwizard.cassandra.ssl;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.internal.core.ssl.SniSslEngineFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class SniSslFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SSLOptionsFactory> factory =
            new YamlConfigurationFactory<>(SSLOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(SniSslFactory.class);
    }

    @Test
    void shouldBuildSniSSLOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/ssl/sni.yaml").toURI());
        final SSLOptionsFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(SniSslFactory.class);
        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        assertThat(profile.getString(DefaultDriverOption.SSL_ENGINE_FACTORY_CLASS))
                .isEqualTo(SniSslEngineFactory.class.getName());
    }
}
