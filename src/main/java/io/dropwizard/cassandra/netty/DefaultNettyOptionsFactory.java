package io.dropwizard.cassandra.netty;

import com.datastax.driver.core.NettyOptions;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link NettyOptions} instances.
 */
@JsonTypeName("default")
public class DefaultNettyOptionsFactory implements NettyOptionsFactory {
    @Override
    public NettyOptions build() {
        return NettyOptions.DEFAULT_INSTANCE;
    }
}
