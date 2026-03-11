# A-AND-I-CONFIG-SERVER

Standalone Spring Cloud Config Server for centralized configuration and secret delivery across A&I services.

This repository provides a dedicated configuration server that can serve shared properties, service-specific settings, and secret-backed values through Spring Cloud Config.

## Stack

- Kotlin
- Spring Boot 4.0.3
- Spring Cloud Config Server
- Java 21

## Features

- Standalone Config Server application enabled with `@EnableConfigServer`
- Local development profile backed by `./local-config-repo`
- Expansion path for Git and AWS Secrets Manager backends
- Basic Auth protection for config endpoints with public health checks

## Local development

The default profile is `local`, which activates the `native` backend and reads configuration files from `./local-config-repo`.

```bash
./gradlew bootRun
```

Health check:

```bash
curl http://localhost:8888/actuator/health
```

Config lookup:

```bash
curl -u config-user:change-this-before-deploy http://localhost:8888/auth/default
```

## Configuration profiles

- `local`: `native` backend using `./local-config-repo`
- `aws`: `git` + `awssecretsmanager` backend composition

## Environment variables

- `SERVER_PORT`: server port, default `8888`
- `CONFIG_SERVER_USERNAME`: Basic Auth username
- `CONFIG_SERVER_PASSWORD`: Basic Auth password
- `CONFIG_GIT_URI`: Git backend repository URI
- `CONFIG_GIT_DEFAULT_LABEL`: Git backend branch or label
- `AWS_REGION`: AWS region for Secrets Manager
- `CONFIG_AWS_SECRETS_PREFIX`: secret prefix path

See `.env.example` for the local bootstrap values.
