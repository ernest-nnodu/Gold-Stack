# Gold Stack

Gold Stack is a small Spring Boot REST API that returns simple JSON messages. It is set up with Java 21, Maven, Docker, Docker Compose, PostgreSQL for local/container profiles, and H2 for tests.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- PostgreSQL
- H2 test database
- Maven Wrapper
- Docker and Docker Compose

## Project Structure

```text
src/main/java/com/jackalcode/gold_stack
├── GoldStackApplication.java
├── controller/MessageController.java
└── entity/MessageEntity.java

src/main/resources
├── application.yml
├── application-dev.yml
├── application-docker.yml
└── application-prod.yml
```

## Requirements

- Java 21
- Docker Desktop, if running with containers

You do not need a local Maven installation because the project includes the Maven Wrapper.

## Run Locally

Start PostgreSQL first. The included Docker Compose file exposes Postgres on local port `5434`.

```powershell
docker compose up postgres
```

In another terminal, run the application with the `dev` profile:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at:

```text
http://localhost:8080
```

## Run With Docker Compose

Build and start the API and PostgreSQL together:

```powershell
docker compose up --build
```

The API container uses the `docker` Spring profile and connects to the `postgres` service inside the Compose network.

## API Endpoints

| Method | Path | Status | Response |
| --- | --- | --- | --- |
| `GET` | `/` | `200 OK` | `{"message":"Hello From Gold Stack in AWS!"}` |
| `GET` | `/error` | `502 Bad Gateway` | `{"message":"This is an error message from Gold Stack in AWS!"}` |
| `GET` | `/custom/**` | `404 Not Found` | `{"message":"Message not found in Gold Stack in AWS!"}` |

Example:

```powershell
curl http://localhost:8080/
```

## Public Endpoints

- **[Home](https://go-32576b666bf441d0a1d2c111a15f8a16.ecs.eu-west-2.on.aws)**
- **[Error Page](https://go-32576b666bf441d0a1d2c111a15f8a16.ecs.eu-west-2.on.aws/error)**
- **[Not Found Page](https://go-32576b666bf441d0a1d2c111a15f8a16.ecs.eu-west-2.on.aws/custom/anything)**

## Configuration Profiles

| Profile | File | Purpose |
| --- | --- | --- |
| default | `application.yml` | Base app settings, including port `8080` |
| dev | `application-dev.yml` | Local PostgreSQL connection on `localhost:5434` |
| docker | `application-docker.yml` | Container PostgreSQL connection on `postgres:5432` |
| prod | `application-prod.yml` | Production database settings from environment variables |
| test | `application-test.yml` | In-memory H2 database for tests |

Production expects these environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

## Run Tests

```powershell
.\mvnw.cmd test
```

The test profile uses an in-memory H2 database.

## Build

Create a packaged JAR:

```powershell
.\mvnw.cmd clean package
```

Build the Docker image:

```powershell
docker build -t gold-stack .
```

## Notes

- The application currently exposes message endpoints only.
- `spring.jpa.hibernate.ddl-auto` is set to `validate` in the base configuration, so a real database schema must already match any JPA mappings when using PostgreSQL.
