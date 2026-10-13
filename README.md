# JobTracker

A job application tracking REST API built with Java and Spring Boot.

> Work in progress.

## Stack

Java 25 · Spring Boot · PostgreSQL · Spring Data JPA · Flyway  
Docker Compose · Maven · JUnit · Mockito · Testcontainers

## Features

- Create, view, and delete job applications
- Update application status
- Filter by status and paginate results
- Validate requests and handle errors
- Persist data in PostgreSQL
- Run automated unit and database integration tests

## Run Locally

Requires JDK 25 and Docker. Run commands from the project root.

1. Copy `.env.example` to `.env` and set your local database credentials.
2. Start PostgreSQL:

   ```powershell
   docker compose up -d
   ```

3. In IntelliJ, configure the application:
    - Environment variable: `DB_PASSWORD=<your database password>`
    - VM option: `-Duser.timezone=UTC`

4. Run `JobtrackerApplication`.

The default database URL is `jdbc:postgresql://localhost:5433/jobtracker`,
with username `jobtracker`. Override these using `DB_URL` and
`DB_USERNAME` if needed.

Docker Compose reads `.env`; the Java application requires its
environment variables to be configured separately.

API base URL: `http://localhost:8080/api/applications`

## Tests

With Docker running:

```powershell
.\mvnw.cmd clean verify
```

Integration tests use an isolated PostgreSQL container.

## Project Status

Backend development is ongoing. Frontend, authentication, and deployment
are planned. Full API documentation will be added as the project matures.