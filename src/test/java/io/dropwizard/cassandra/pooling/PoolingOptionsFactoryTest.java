package io.dropwizard.cassandra.pooling;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.util.Duration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class PoolingOptionsFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<PoolingOptionsFactory> factory =
            new YamlConfigurationFactory<>(PoolingOptionsFactory.class, validator, objectMapper, "dw");

    @Test
    public void shouldBuildPoolingOptions() throws URISyntaxException, IOException, ConfigurationException {
        final File yaml = new File(Resources.getResource("smoke/pooling/pooling-options.yaml").toURI());
        final PoolingOptionsFactory factory = this.factory.build(yaml);


        assertThat(factory.getMaxRequestsPerConnection()).isEqualTo(5);
        assertThat(factory.getMaxLocalConnections()).isEqualTo(20);
        assertThat(factory.getMaxRemoteConnections()).isEqualTo(10);
        assertThat(factory.getHeartbeatInterval()).isEqualTo(Duration.seconds(5));
        assertThat(factory.getConnectionConnectTimeout()).isEqualTo(Duration.seconds(10));

        DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();
        factory.accept(builder);
        DriverExecutionProfile profile = builder.build().getInitialConfig().getDefaultProfile();

        assertThat(profile.getInt(DefaultDriverOption.CONNECTION_MAX_REQUESTS)).isEqualTo(5);
        assertThat(profile.getInt(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE)).isEqualTo(20);
        assertThat(profile.getInt(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE)).isEqualTo(10);
        assertThat(profile.getInt(DefaultDriverOption.HEARTBEAT_INTERVAL)).isEqualTo(5000);
        assertThat(profile.getInt(DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT)).isEqualTo(10000);
    }
}
