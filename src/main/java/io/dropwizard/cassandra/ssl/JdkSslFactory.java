package io.dropwizard.cassandra.ssl;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.ssl.JdkSslHandlerFactory;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

@JsonTypeName("jdk")
public class JdkSslFactory implements SSLOptionsFactory {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.SSL_ENGINE_FACTORY_CLASS, JdkSslHandlerFactory.class);
    }
}
