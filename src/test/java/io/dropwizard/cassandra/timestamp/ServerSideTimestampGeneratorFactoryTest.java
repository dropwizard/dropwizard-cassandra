package io.dropwizard.cassandra.timestamp;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.time.ServerSideTimestampGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import jakarta.validation.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerSideTimestampGeneratorFactoryTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<TimestampGeneratorFactory> factory =
            new YamlConfigurationFactory<>(TimestampGeneratorFactory.class, validator, objectMapper, "dw");
    private final DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

    @Test
    void shouldBuildAnAtomicMonotonicTimestampGenerator() throws URISyntaxException, IOException,
            ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/timestamp/server-side.yaml")
                .toURI());
        final TimestampGeneratorFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ServerSideTimestampGeneratorFactory.class));
        factory.accept(builder);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS),
                ServerSideTimestampGenerator.class.getName());
    }

    @Test
    void buildsTimestampGenerator() throws Exception {
        final TimestampGeneratorFactory factory = new ServerSideTimestampGeneratorFactory();

        factory.accept(builder);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS),
                ServerSideTimestampGenerator.class.getName());
    }

    @Test
    void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ServerSideTimestampGeneratorFactory.class));
    }
}
