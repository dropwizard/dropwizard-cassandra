package io.dropwizard.cassandra.netty;

import com.datastax.driver.core.NettyOptions;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface NettyOptionsFactory extends Discoverable {
    NettyOptions build();
}
