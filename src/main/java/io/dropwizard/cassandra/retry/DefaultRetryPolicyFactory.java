package io.dropwizard.cassandra.retry;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.retry.DefaultRetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("default")
public class DefaultRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public void build(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.RETRY_POLICY_CLASS, DefaultRetryPolicy.class);
    }
}
