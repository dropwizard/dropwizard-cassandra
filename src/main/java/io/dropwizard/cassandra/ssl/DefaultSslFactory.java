package io.dropwizard.cassandra.ssl;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.ssl.DefaultSslEngineFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

import java.util.List;

@JsonTypeName("default")
public class DefaultSslFactory implements SSLOptionsFactory {
    @JsonProperty
    private List<String> cipherSuites;
    @JsonProperty
    private Boolean hostValidation;
    @JsonProperty
    private String keyStorePassword;
    @JsonProperty
    private String keyStorePath;
    @JsonProperty
    private String trustStorePassword;
    @JsonProperty
    private String trustStorePath;

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Boolean getHostValidation() {
        return hostValidation;
    }

    public void setHostValidation(Boolean hostValidation) {
        this.hostValidation = hostValidation;
    }

    public List<String> getCipherSuites() {
        return cipherSuites;
    }

    public void setCipherSuites(List<String> cipherSuites) {
        this.cipherSuites = cipherSuites;
    }

    @Override
    public void build(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.SSL_ENGINE_FACTORY_CLASS, DefaultSslEngineFactory.class)
                .withNullSafeStringList(DefaultDriverOption.SSL_CIPHER_SUITES, cipherSuites)
                .withNullSafeBoolean(DefaultDriverOption.SSL_HOSTNAME_VALIDATION, hostValidation)
                .withString(DefaultDriverOption.SSL_KEYSTORE_PASSWORD, keyStorePassword)
                .withString(DefaultDriverOption.SSL_KEYSTORE_PATH, keyStorePath)
                .withString(DefaultDriverOption.SSL_TRUSTSTORE_PASSWORD, trustStorePassword)
                .withString(DefaultDriverOption.SSL_TRUSTSTORE_PATH, trustStorePath);
    }
}
