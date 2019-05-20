package io.dropwizard.cassandra.ssl;

import com.datastax.driver.core.RemoteEndpointAwareNettySSLOptions;
import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.ValidationMethod;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import java.io.File;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.RemoteEndpointAwareNettySSLOptions} instances.
 */
@JsonTypeName("netty")
public class RemoteEndpointAwareNettySSLOptionsFactory implements SSLOptionsFactory {

    @JsonProperty
    private SslProvider provider;
    @JsonProperty
    private List<String> ciphers;
    @JsonProperty
    private ClientAuth clientAuth;
    @JsonProperty
    private Long sessionCacheSize;
    @JsonProperty
    private Duration sessionTimeout;
    @JsonProperty
    private File trustCertChainFile;
    @Valid
    @JsonProperty
    private KeyManagerConfig keyManager;

    public SslProvider getProvider() {
        return provider;
    }

    public void setProvider(SslProvider provider) {
        this.provider = provider;
    }

    public List<String> getCiphers() {
        return ciphers;
    }

    public void setCiphers(List<String> ciphers) {
        this.ciphers = ciphers;
    }

    public ClientAuth getClientAuth() {
        return clientAuth;
    }

    public void setClientAuth(ClientAuth clientAuth) {
        this.clientAuth = clientAuth;
    }

    public Long getSessionCacheSize() {
        return sessionCacheSize;
    }

    public void setSessionCacheSize(Long sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

    public Duration getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public File getTrustCertChainFile() {
        return trustCertChainFile;
    }

    public void setTrustCertChainFile(File trustCertChainFile) {
        this.trustCertChainFile = trustCertChainFile;
    }

    public KeyManagerConfig getKeyManager() {
        return keyManager;
    }

    public void setKeyManager(KeyManagerConfig keyManager) {
        this.keyManager = keyManager;
    }

    @ValidationMethod(message = "must define keyManager when clientAuth is REQUIRE")
    public boolean isClientAuthConfigValid() {
        return clientAuth != ClientAuth.REQUIRE || keyManager != null;
    }

    @Override
    public SSLOptions build() {
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

        if (provider != null) {
            sslContextBuilder.sslProvider(provider);
        }

        if (ciphers != null) {
            sslContextBuilder.ciphers(ciphers);
        }

        if (clientAuth != null) {
            sslContextBuilder.clientAuth(clientAuth);
        }

        if (sessionCacheSize != null) {
            sslContextBuilder.sessionCacheSize(sessionCacheSize);
        }

        if (sessionTimeout != null) {
            sslContextBuilder.sessionTimeout(sessionTimeout.toSeconds());
        }

        if (trustCertChainFile != null) {
            sslContextBuilder.trustManager(trustCertChainFile);
        }

        if (keyManager != null) {
            sslContextBuilder.keyManager(
                    keyManager.getKeyCertChainFile(),
                    keyManager.getKeyFile(),
                    keyManager.getKeyPassword());
        }

        SslContext sslContext;
        try {
            sslContext = sslContextBuilder.build();
        } catch (SSLException e) {
            throw new RuntimeException("Unable to build Netty SslContext", e);
        }

        return new RemoteEndpointAwareNettySSLOptions(sslContext);
    }

    static class KeyManagerConfig {

        @NotNull
        @JsonProperty
        private File keyCertChainFile;

        @NotNull
        @JsonProperty
        private File keyFile;

        @JsonProperty
        private String keyPassword;

        public File getKeyCertChainFile() {
            return keyCertChainFile;
        }

        public void setKeyCertChainFile(File keyCertChainFile) {
            this.keyCertChainFile = keyCertChainFile;
        }

        public File getKeyFile() {
            return keyFile;
        }

        public void setKeyFile(File keyFile) {
            this.keyFile = keyFile;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }
    }
}
