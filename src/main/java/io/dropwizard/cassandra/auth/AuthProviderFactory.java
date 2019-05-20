package io.dropwizard.cassandra.auth;

import com.datastax.driver.core.AuthProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface AuthProviderFactory extends Discoverable {
    AuthProvider build();
}
