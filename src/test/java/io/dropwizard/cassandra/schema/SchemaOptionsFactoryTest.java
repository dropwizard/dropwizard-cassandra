package io.dropwizard.cassandra.schema;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static io.dropwizard.cassandra.schema.SchemaOptionsFactory.DEFAULT_SCHEMA_AGREEMENT_INTERVAL_MILLISECONDS;
import static io.dropwizard.cassandra.schema.SchemaOptionsFactory.DEFAULT_SCHEMA_AGREEMENT_TIMEOUT_SECONDS;
import static io.dropwizard.cassandra.schema.SchemaOptionsFactory.DEFAULT_SCHEMA_AGREEMENT_WARN_ON_FAILURE;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<SchemaOptionsFactory> factory =
            new YamlConfigurationFactory<>(SchemaOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildDefaultSchemaOptions() throws URISyntaxException, IOException, ConfigurationException {
        // Load a default copy of the options factory
        final SchemaOptionsFactory factory = this.factory.build();

        // Validate that the factory contains values as expected
        assertThat(factory.getAgreementIntervalMilliseconds())
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_INTERVAL_MILLISECONDS);
        assertThat(factory.getAgreementTimeoutSeconds())
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_TIMEOUT_SECONDS);
        assertThat(factory.getAgreementWarnOnFailure())
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_WARN_ON_FAILURE);

        // Validate that the driver configuration contains the values as expected
        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        assertThat(profile.getInt(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL))
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_INTERVAL_MILLISECONDS);
        assertThat(profile.getInt(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT))
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_TIMEOUT_SECONDS);
        assertThat(profile.getBoolean(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN))
                .isEqualTo(DEFAULT_SCHEMA_AGREEMENT_WARN_ON_FAILURE);
    }

    @Test
    public void shouldBuildSchemaOptions() throws URISyntaxException, IOException, ConfigurationException {
        // Load a customized copy of the optiosn factory as defined in the yaml file
        final File yaml = new File(Resources.getResource("smoke/schema/schema-options.yaml").toURI());
        final SchemaOptionsFactory factory = this.factory.build(yaml);

        // Validate that the factory contains values as expected
        assertThat(factory.getAgreementIntervalMilliseconds())
                .isEqualTo(400);
        assertThat(factory.getAgreementTimeoutSeconds())
                .isEqualTo(20);
        assertThat(factory.getAgreementWarnOnFailure())
                .isEqualTo(false);

        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        // Validate that the driver configuration contains the values as expected
        assertThat(profile.getInt(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL))
                .isEqualTo(400);
        assertThat(profile.getInt(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT))
                .isEqualTo(20);
        assertThat(profile.getBoolean(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN))
                .isEqualTo(false);
    }
}
