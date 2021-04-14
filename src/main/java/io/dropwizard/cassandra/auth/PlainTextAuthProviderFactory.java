package io.dropwizard.cassandra.auth;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.auth.PlainTextAuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import javax.validation.constraints.NotNull;

@JsonTypeName("plain-text")
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
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.AUTH_PROVIDER_CLASS, PlainTextAuthProvider.class)
                .withString(DefaultDriverOption.AUTH_PROVIDER_USER_NAME, username)
                .withString(DefaultDriverOption.AUTH_PROVIDER_PASSWORD, password);
    }
}
