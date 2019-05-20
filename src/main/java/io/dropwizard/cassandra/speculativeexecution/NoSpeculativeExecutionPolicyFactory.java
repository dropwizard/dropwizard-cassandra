package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.NoSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("none")
public class NoSpeculativeExecutionPolicyFactory implements SpeculativeExecutionPolicyFactory {
    @Override
    public SpeculativeExecutionPolicy build() {
        return NoSpeculativeExecutionPolicy.INSTANCE;
    }
}
