package io.dropwizard.cassandra;

import io.dropwizard.core.Configuration;

/**
 * Provides access to Cassandra configuration and factory based on it.
 */
public interface CassandraConfiguration<T extends Configuration> {
	CassandraFactory getCassandraFactory(T configuration);
}
