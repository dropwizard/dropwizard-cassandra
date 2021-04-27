package io.dropwizard.cassandra.protocolVersion;

import com.datastax.oss.driver.api.core.DefaultProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("default")
public class DefaultProtocolVersionFactory implements ProtocolVersionFactory {
    private DefaultProtocolVersion version;

    public DefaultProtocolVersion getVersion() {
        return version;
    }

    public void setVersion(DefaultProtocolVersion version) {
        this.version = version;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder configLoaderBuilder) {
        configLoaderBuilder.withNullSafeString(DefaultDriverOption.PROTOCOL_VERSION, version.name());
    }
}
