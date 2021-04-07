package io.dropwizard.cassandra.timestamp;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.time.AtomicTimestampGenerator;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AtomicMonotonicTimestampGeneratorFactoryTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<TimestampGeneratorFactory> factory =
            new YamlConfigurationFactory<>(TimestampGeneratorFactory.class, validator, objectMapper, "dw");
    private final DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

    @Test
    public void shouldBuildAnAtomicMonotonicTimestampGenerator() throws URISyntaxException, IOException,
            ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/timestamp/atomic-monotonic.yaml")
                .toURI());
        final TimestampGeneratorFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(AtomicMonotonicTimestampGeneratorFactory.class));
        factory.build(builder);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS),
                AtomicTimestampGenerator.class.getName());
    }

    @Test
    public void buildsTimestampGenerator() throws Exception {
        final TimestampGeneratorFactory factory = new AtomicMonotonicTimestampGeneratorFactory();

        factory.build(builder);
        assertEquals(builder.build().getInitialConfig().getDefaultProfile()
                        .getString(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS),
                AtomicTimestampGenerator.class.getName());
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(AtomicMonotonicTimestampGeneratorFactory.class));
    }
}
