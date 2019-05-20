CREATE KEYSPACE IF NOT EXISTS dropwizard_test
WITH REPLICATION = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};
