package io.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ReconnectionPolicyFactory extends Discoverable {
    ReconnectionPolicy build();
}
