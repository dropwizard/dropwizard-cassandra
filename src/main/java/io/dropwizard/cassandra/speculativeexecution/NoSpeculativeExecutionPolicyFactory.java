package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.specex.NoSpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("none")
public class NoSpeculativeExecutionPolicyFactory implements SpeculativeExecutionPolicyFactory {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS, NoSpeculativeExecutionPolicy.class);
    }
}
