package io.dropwizard.cassandra;

import brave.Tracing;
import com.datastax.driver.core.Session;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class CassandraBundle<T extends Configuration> implements ConfiguredBundle<T>, CassandraConfiguration<T> {
    @Nullable
    private Session session;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final CassandraFactory cassandraFactory = requireNonNull(getCassandraFactory(configuration));

        final Tracing tracing = Tracing.current();

        this.session = cassandraFactory.build(environment.metrics(), environment.lifecycle(), environment.healthChecks(), tracing);
    }

    public abstract CassandraFactory getCassandraFactory(T configuration);

    public Session getSession() {
        return requireNonNull(session);
    }
}
