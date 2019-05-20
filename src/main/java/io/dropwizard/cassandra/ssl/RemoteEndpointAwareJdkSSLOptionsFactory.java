package io.dropwizard.cassandra.ssl;

import com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions;
import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions} instances.
 */
@JsonTypeName("jdk")
public class RemoteEndpointAwareJdkSSLOptionsFactory implements SSLOptionsFactory {

    @Override
    public SSLOptions build() {
        return RemoteEndpointAwareJdkSSLOptions.builder().build();
    }
}
