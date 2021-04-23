package io.dropwizard.cassandra.network;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.addresstranslation.Ec2MultiRegionAddressTranslator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.cassandra.DropwizardProgrammaticDriverConfigLoaderBuilder;

/**
 * A factory for configuring and building {@link Ec2MultiRegionAddressTranslator} instances.
 */
@JsonTypeName("ec2-multi-region")
public class EC2MultiRegionAddressTranslatorFactory implements AddressTranslatorFactory {
    @Override
    public void accept(DropwizardProgrammaticDriverConfigLoaderBuilder builder) {
        builder.withClass(DefaultDriverOption.ADDRESS_TRANSLATOR_CLASS, Ec2MultiRegionAddressTranslator.class);
    }
}
