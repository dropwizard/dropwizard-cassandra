package io.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface RetryPolicyFactory extends Discoverable {
    RetryPolicy build();
}
