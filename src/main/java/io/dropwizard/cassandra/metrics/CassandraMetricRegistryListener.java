package io.dropwizard.cassandra.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.Timer;

import static java.util.Objects.requireNonNull;

public class CassandraMetricRegistryListener implements MetricRegistryListener {
    private final MetricRegistry metrics;
    private final String metricPrefix;

    public CassandraMetricRegistryListener(final MetricRegistry metrics,
                                           final String metricPrefix) {
        this.metrics = requireNonNull(metrics);
        this.metricPrefix = requireNonNull(metricPrefix);
    }

    @Override
    public void onGaugeAdded(final String name, final Gauge<?> gauge) {
        registerMetric(name, gauge);
    }

    @Override
    public void onGaugeRemoved(final String name) {
        metrics.remove(name);
    }

    @Override
    public void onCounterAdded(final String name, final Counter counter) {
        registerMetric(name, counter);
    }

    @Override
    public void onCounterRemoved(final String name) {
        metrics.remove(name);
    }

    @Override
    public void onHistogramAdded(final String name, final Histogram histogram) {
        registerMetric(name, histogram);
    }

    @Override
    public void onHistogramRemoved(final String name) {
        metrics.remove(name);
    }

    @Override
    public void onMeterAdded(final String name, final Meter meter) {
        registerMetric(name, meter);
    }

    @Override
    public void onMeterRemoved(final String name) {
        metrics.remove(name);
    }

    @Override
    public void onTimerAdded(final String name, final Timer timer) {
        registerMetric(name, timer);
    }

    @Override
    public void onTimerRemoved(final String name) {
        metrics.remove(name);
    }

    private void registerMetric(final String name, final Metric metric) {
        final String metricName = MetricRegistry.name(metricPrefix, name);
        if (!metrics.getNames().contains(metricName)) {
            metrics.register(metricName, metric);
        }
    }
}
