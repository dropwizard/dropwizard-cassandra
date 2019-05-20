package io.dropwizard.cassandra.timestamp;

import com.datastax.driver.core.TimestampGenerator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface TimestampGeneratorFactory extends Discoverable {
    TimestampGenerator build();
}
