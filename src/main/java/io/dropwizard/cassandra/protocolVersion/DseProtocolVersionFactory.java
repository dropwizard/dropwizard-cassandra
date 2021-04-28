package io.dropwizard.cassandra.protocolVersion;

import com.datastax.dse.driver.api.core.DseProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("dse")
public class DseProtocolVersionFactory implements ProtocolVersionFactory {
    private DseProtocolVersion version;

    public DseProtocolVersion getVersion() {
        return version;
    }

    public void setVersion(DseProtocolVersion version) {
        this.version = version;
    }

    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder configLoaderBuilder) {
        configLoaderBuilder.withNullSafeString(DefaultDriverOption.PROTOCOL_VERSION, version.name());
    }
}
