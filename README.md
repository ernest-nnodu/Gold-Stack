# Gold Stack

Gold Stack is a small Spring Boot REST API that returns and persists simple JSON messages. This repository is set up for local development, containerized runs, automated database migrations, and monitoring. It includes unit and integration tests (Testcontainers), Flyway migrations, and observability via Spring Boot Actuator + Prometheus + Grafana.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web 
- Spring Data JPA
- PostgreSQL
- Flyway for DB migrations
- Spring Boot Actuator + Micrometer Prometheus registry
- Testcontainers for integration tests
- Maven (Maven Wrapper included)
- Docker & Docker Compose

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

## Run with Docker Compose (app + Postgres + Prometheus + Grafana)
- Build and start everything:

```powershell
docker compose up --build
```

- Services:
    - App at http://localhost:8080
    - Prometheus UI at http://localhost:9090
    - Grafana at http://localhost:3000 (default Grafana credentials may be required — see Grafana docs)

The API container uses the `docker` Spring profile and connects to the `postgres` service inside the Compose network.

## API Endpoints

| Method | Path             | Status                     | Response                                                         |
| --- |------------------|----------------------------|------------------------------------------------------------------|
| `GET` | `/`              | `200 OK`                   | `{"message":"Hello From Gold Stack in AWS!"}`                    |
| `GET` | `/error`         | `502 Bad Gateway`          | `{"message":"This is an error message from Gold Stack in AWS!"}` |
| `GET` | `/custom/**`     | `404 Not Found`            | `{"message":"Message not found in Gold Stack in AWS!"}`          |
| `GET` | `/messages`      | `200 OK`                   | `{"messages": [...]}`                                            |
| `GET` | `/messages/{id}` | `200 Ok or 404 Not Found`  | `{"message": "A single persisted message"}`                      |
| `POST` | `/messages`      | `201 Created`              | `{"message": "Creates and returns a new message"}`               |


Example:

```powershell
curl http://localhost:8080/
```

## API Endpoints (examples)

- GET /
    - 200 OK
    - Response example:
  ```json
  {
    "id": 0,
    "title": "Happy Message",
    "content": "Hello From Gold Stack in AWS!",
    "createdAt": "2024-01-01T00:00:00Z"
  }
  ```

- GET /messages
    - 200 OK — list of messages (persisted)
    - Example curl:
  ```powershell
  curl http://localhost:8080/messages
  ```

- GET /messages/{id}
    - 200 OK — single message
    - 404 if not found

- POST /messages
    - 201 Created — creates and returns message
    - Request JSON:
  ```json
  {
    "title": "My Title",
    "content": "The message content"
  }
  ```
    - Example curl:
  ```powershell
  curl -X POST http://localhost:8080/messages -H "Content-Type: application/json" -d "{\"title\":\"Hi\",\"content\":\"Hello\"}"
  ```

- GET /error
    - Demonstrates a 502 Bad Gateway response with a JSON body.

- GET /custom/**
    - Demonstrates a 404 Not Found example response with a JSON body.

- Monitoring / Actuator
  - Available actuator endpoints (exposed): `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
  - Prometheus scrape endpoint: `http://<host>:8080/actuator/prometheus`
  - Docker Compose config wires Prometheus to scrape metrics and Grafana can be configured to visualize them.


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
