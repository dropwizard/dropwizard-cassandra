package io.dropwizard.cassandra.timestamp;

import com.datastax.driver.core.ServerSideTimestampGenerator;
import com.datastax.driver.core.TimestampGenerator;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class ServerSideTimestampGeneratorFactoryTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<TimestampGeneratorFactory> factory =
            new YamlConfigurationFactory<>(TimestampGeneratorFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildAnAtomicMonotonicTimestampGenerator() throws URISyntaxException, IOException,
            ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/timestamp/server-side.yaml")
                .toURI());
        final TimestampGeneratorFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ServerSideTimestampGeneratorFactory.class));
        assertThat(factory.build(), instanceOf(ServerSideTimestampGenerator.class));
    }

    @Test
    public void buildsTimestampGenerator() throws Exception {
        final TimestampGeneratorFactory factory = new ServerSideTimestampGeneratorFactory();

        final TimestampGenerator policy = factory.build();

        assertThat(policy, is(instanceOf(ServerSideTimestampGenerator.class)));
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ServerSideTimestampGeneratorFactory.class));
    }
}
