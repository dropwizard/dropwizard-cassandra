package io.dropwizard.cassandra.test.smoke;

import brave.Tracing;
import brave.propagation.StrictScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.datastax.oss.driver.api.core.CqlSession;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.junit.jupiter.api.AfterAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmokeTestApp extends Application<SmokeTestConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(SmokeTestApp.class);

    private static final Tracing tracing = Tracing.newBuilder()
            .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                    .addScopeDecorator(StrictScopeDecorator.create())
                    .build())
            .build();

    private CqlSession session;

    @AfterAll
    static void tearDownAfterClass() {
        tracing.close();
    }

    @Override
    public void initialize(final Bootstrap<SmokeTestConfiguration> bootstrap) {}

    @Override
    public void run(final SmokeTestConfiguration configuration, final Environment environment) {
        log.debug("Running smoke test");

        this.session = configuration.getCassandraFactory().build(environment.metrics(), environment.lifecycle(),
                environment.healthChecks(), tracing);
    }

    public CqlSession getSession() {
        return session;
    }
}
