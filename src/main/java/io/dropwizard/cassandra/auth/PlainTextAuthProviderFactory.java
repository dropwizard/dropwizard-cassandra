package io.dropwizard.cassandra.auth;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotNull;

@JsonTypeName("plainText")
public class PlainTextAuthProviderFactory implements AuthProviderFactory {
    @NotNull
    @JsonProperty
    private String username;
    @NotNull
    @JsonProperty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public AuthProvider build() {
        return new PlainTextAuthProvider(username, password);
    }
}
