package io.dropwizard.cassandra.test.smoke;

import brave.Tracing;
import brave.propagation.StrictScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmokeTestApp extends Application<SmokeTestConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(SmokeTestApp.class);

    private static final Tracing tracing = Tracing.newBuilder()
            .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                    .addScopeDecorator(StrictScopeDecorator.create())
                    .build())
            .build();

    private Cluster cluster;
    private Session session;

    @AfterClass
    public static void tearDownAfterClass() {
        tracing.close();
    }

    @Override
    public void initialize(final Bootstrap<SmokeTestConfiguration> bootstrap) {}

    @Override
    public void run(final SmokeTestConfiguration configuration, final Environment environment) {
        log.debug("Running smoke test for {}", configuration.getCassandraFactory().getClusterName());

        this.session = configuration.getCassandraFactory().build(environment.metrics(), environment.lifecycle(),
                environment.healthChecks(), tracing);
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Session getSession() {
        return session;
    }
}
