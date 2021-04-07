# dropwizard-cassandra

[![Build](https://github.com/dropwizard/dropwizard-cassandra/workflows/Build/badge.svg)](https://github.com/dropwizard/dropwizard-cassandra/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=dropwizard_dropwizard-cassandra&metric=alert_status)](https://sonarcloud.io/dashboard?id=dropwizard_dropwizard-cassandra)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.dropwizard.modules/dropwizard-cassandra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.dropwizard.modules/dropwizard-cassandra/)

Provides easy integration for Dropwizard applications with the Datastax Cassandra java driver..

This bundle comes with out-of-the-box support for:
* YAML-based configuration and built-in strongly typed configuration validation
* Cluster connection lifecycle management
* Cluster health checks
* Metrics integration for Cassandra driver stats using your applications `MetricRegistry`

## Future improvements
* Schema migrations functionality, similar to what flyway and liquibase offer for SQL databases.

## Dropwizard Version Support Matrix
dropwizard-cassandra    | Dropwizard v1.3.x  | Dropwizard v2.0.x
----------------------- | ------------------ | ------------------
v1.3.x                  | :white_check_mark: | :white_check_mark:
v1.4.x                  | :white_check_mark: | :white_check_mark:

## Usage
Add dependency on library.

Maven:
```xml
<dependency>
  <groupId>io.dropwizard.modules</groupId>
  <artifactId>dropwizard-cassandra</artifactId>
  <version>2.0.0</version>
</dependency>
```

Gradle:
```groovy
compile "io.dropwizard.modules:dropwizard-cassandra:2.0.0"
```

### Usage

Minimal example configuration:
```
cassandra:
  type: basic
  name: cassandra.test-cluster
  clusterName: test-cluster
  contactPoints:
    - localhost
  connectionString: localhost
  port: 9042
  loadBalancingPolicy:
    type: roundRobin
```

### Acknowledgements
This library was originally built to extend https://github.com/composable-systems/dropwizard-cassandra, but was forked due to some
needs that were unmet (for instance, being able to define different `CassandraFactory` implementations, if a modified client is used), and
due to lack of maintenance. Big thanks to @stuartgunter for his module, which inspired this one heavily, and for which this module borrows
a lot of ideas and code.
