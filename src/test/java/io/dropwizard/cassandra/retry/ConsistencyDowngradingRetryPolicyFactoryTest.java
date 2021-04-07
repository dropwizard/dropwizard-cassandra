package io.dropwizard.cassandra.retry;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.retry.ConsistencyDowngradingRetryPolicy;
import com.datastax.oss.driver.internal.core.retry.DefaultRetryPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ConsistencyDowngradingRetryPolicyFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<RetryPolicyFactory> factory =
            new YamlConfigurationFactory<>(RetryPolicyFactory.class, validator, objectMapper, "dw");
    private final DropwizardProgrammaticDriverConfigLoaderBuilder builder = DropwizardProgrammaticDriverConfigLoaderBuilder.newInstance();

    @Test
    public void shouldBuildAConfigurableRetryPolicy() throws URISyntaxException, IOException, ConfigurationException {
        final File yml = new File(Resources.getResource("smoke/retry/consistency-downgrading.yaml").toURI());
        final RetryPolicyFactory factory = this.factory.build(yml);
        assertThat(factory, instanceOf(ConsistencyDowngradingRetryPolicyFactory.class));
        factory.build(builder);
        Assertions.assertThat(builder.build().getInitialConfig().getDefaultProfile().getString(DefaultDriverOption.RETRY_POLICY_CLASS))
                .isEqualTo(ConsistencyDowngradingRetryPolicy.class.getName());
    }

    @Test
    public void isDiscoverable() {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes(),
                hasItem(ConsistencyDowngradingRetryPolicyFactory.class));
    }
}
