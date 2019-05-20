package io.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SpeculativeExecutionPolicyFactory extends Discoverable {
    SpeculativeExecutionPolicy build();
}
