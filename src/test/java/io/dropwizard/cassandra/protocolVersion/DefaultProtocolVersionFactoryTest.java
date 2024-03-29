package io.dropwizard.cassandra.protocolVersion;

import com.datastax.oss.driver.api.core.DefaultProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.internal.core.ssl.JdkSslHandlerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.cassandra.ssl.JdkSslFactory;
import io.dropwizard.cassandra.ssl.SSLOptionsFactory;
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

class DefaultProtocolVersionFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<ProtocolVersionFactory> factory =
            new YamlConfigurationFactory<>(ProtocolVersionFactory.class, validator, objectMapper, "dw");

    @Test
    void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(DefaultProtocolVersionFactory.class);
    }

    @Test
    void shouldBuildDefaultProtocol() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/protocolVersion/default.yaml").toURI());
        final ProtocolVersionFactory factory = this.factory.build(yaml);
        assertThat(factory).isInstanceOf(DefaultProtocolVersionFactory.class);
        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        assertThat(profile.getString(DefaultDriverOption.PROTOCOL_VERSION))
                .isEqualTo(DefaultProtocolVersion.V3.name());
    }
}
