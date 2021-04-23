package io.dropwizard.cassandra;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class DropwizardProgrammaticDriverConfigLoaderBuilder implements ProgrammaticDriverConfigLoaderBuilder {
    private final ProgrammaticDriverConfigLoaderBuilder builder;

    private DropwizardProgrammaticDriverConfigLoaderBuilder(ProgrammaticDriverConfigLoaderBuilder builder) {
        this.builder = builder;
    }

    public static DropwizardProgrammaticDriverConfigLoaderBuilder newInstance() {
        return new DropwizardProgrammaticDriverConfigLoaderBuilder(DriverConfigLoader.programmaticBuilder());
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeString(DefaultDriverOption option, String val) {
        return nullSafSet(builder::withString, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeStringList(DefaultDriverOption option, List<String> val) {
        return nullSafSet(builder::withStringList, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeDuration(DefaultDriverOption option,
                                                                                io.dropwizard.util.Duration val) {
        return nullSafSet((opt, dur) -> builder.withDuration(opt, Duration.ofMillis(val.toMilliseconds())), option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeInteger(DefaultDriverOption option, Integer val) {
        return nullSafSet(builder::withInt, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeClass(DefaultDriverOption option, Class<?> val) {
        return nullSafSet(builder::withClass, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeClassString(DefaultDriverOption option, String val) {

        return nullSafSet((x, y) -> {
            try {
                builder.withClass(x, Class.forName(y));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeBoolean(DefaultDriverOption option, Boolean val) {
        return nullSafSet(builder::withBoolean, option, val);
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withNullSafeLong(DefaultDriverOption option, Long val) {
        return nullSafSet(builder::withLong, option, val);
    }

    private <T> DropwizardProgrammaticDriverConfigLoaderBuilder nullSafSet(BiConsumer<DefaultDriverOption, T> consumer,
                                                                           DefaultDriverOption option, T val) {
        if (Objects.nonNull(val)) {
            consumer.accept(option, val);
        }
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder startProfile(@NonNull String s) {
        builder.startProfile(s);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder endProfile() {
        builder.endProfile();
        return this;
    }

    @NonNull
    @Override
    public DriverConfigLoader build() {
        return builder.build();
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withBoolean(@NonNull DriverOption driverOption, boolean b) {
        builder.withBoolean(driverOption, b);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withBooleanList(@NonNull DriverOption driverOption, @NonNull List<Boolean> list) {
        builder.withBooleanList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withInt(@NonNull DriverOption driverOption, int i) {
        builder.withInt(driverOption, i);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withIntList(@NonNull DriverOption driverOption, @NonNull List<Integer> list) {
        builder.withIntList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withLong(@NonNull DriverOption driverOption, long l) {
        builder.withLong(driverOption, l);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withLongList(@NonNull DriverOption driverOption, @NonNull List<Long> list) {
        builder.withLongList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withDouble(@NonNull DriverOption driverOption, double v) {
        builder.withDouble(driverOption, v);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withDoubleList(@NonNull DriverOption driverOption, @NonNull List<Double> list) {
        builder.withDoubleList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withString(@NonNull DriverOption driverOption, @NonNull String s) {
        builder.withString(driverOption, s);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withStringList(@NonNull DriverOption driverOption, @NonNull List<String> list) {
        builder.withStringList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withStringMap(@NonNull DriverOption driverOption, @NonNull Map<String, String> map) {
        builder.withStringMap(driverOption, map);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withBytes(@NonNull DriverOption driverOption, long l) {
        builder.withBytes(driverOption, l);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withBytesList(@NonNull DriverOption driverOption, @NonNull List<Long> list) {
        builder.withBytesList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withDuration(@NonNull DriverOption driverOption, @NonNull Duration duration) {
        builder.withDuration(driverOption, duration);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder withDurationList(@NonNull DriverOption driverOption, @NonNull List<Duration> list) {
        builder.withDurationList(driverOption, list);
        return this;
    }

    @NonNull
    @Override
    public DropwizardProgrammaticDriverConfigLoaderBuilder without(@NonNull DriverOption driverOption) {
        builder.without(driverOption);
        return this;
    }

    public DropwizardProgrammaticDriverConfigLoaderBuilder withClass(@NonNull DriverOption option, @NonNull Class<?> value) {
        builder.withClass(option, value);
        return this;
    }
}
