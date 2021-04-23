package io.dropwizard.cassandra.retry;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.retry.ConsistencyDowngradingRetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("consistency-downgrading")
public class ConsistencyDowngradingRetryPolicyFactory implements RetryPolicyFactory {

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.RETRY_POLICY_CLASS, ConsistencyDowngradingRetryPolicy.class);
    }
}
