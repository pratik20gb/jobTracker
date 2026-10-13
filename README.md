# JobTracker

A REST API for tracking job applications, built with Java and Spring Boot.

Supports application creation, retrieval, status updates, and deletion, with PostgreSQL persistence, request validation, and automated tests.

> Backend in development. Authentication, frontend, and deployment are pending.

## Tech Stack

- **Java 25** · **Spring Boot 4.1.1**
- Spring MVC · Bean Validation
- Spring Data JPA · Hibernate
- PostgreSQL 17 · Flyway
- Docker Compose · Maven Wrapper
- JUnit · Mockito · Testcontainers

## Features

- Create, list, retrieve, and delete applications
- Update application status
- Validate requests and return field-level errors
- Handle missing applications with `404 Not Found`
- Retain data across application restarts
- Manage database schema through versioned migrations

Supported statuses: `SAVED`, `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED`.

## Getting Started

### Prerequisites

- JDK 25
- Docker Desktop with Docker Compose
- Git

Commands below use Windows PowerShell. Run them from the project root, where `pom.xml` is located.

### 1. Clone the repository

```powershell
git clone https://github.com/pratik20gb/jobTracker.git
cd jobTracker
```

### 2. Configure PostgreSQL

Copy the environment template:

```powershell
Copy-Item .env.example .env
```

Update `.env` with your local credentials:

```dotenv
POSTGRES_DB=jobtracker
POSTGRES_USER=jobtracker
POSTGRES_PASSWORD=choose_a_local_password
```

Replace the placeholder password. Keep `.env` out of version control.

Start the database:

```powershell
docker compose up -d
```

PostgreSQL is available at `localhost:5433`. Data is stored in a named Docker volume.

### 3. Run the application

Open the project in IntelliJ and configure `JobtrackerApplication`:

| Setting | Value |
| --- | --- |
| JDK | `25` |
| Environment variable | `DB_PASSWORD=<your database password>` |
| VM options | `-Duser.timezone=UTC` |

Run the application.

Docker Compose reads `.env`; the Java application does not automatically load it. Set `DB_PASSWORD` separately in the run configuration.

Available application configuration:

| Variable | Default |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5433/jobtracker` |
| `DB_USERNAME` | `jobtracker` |
| `DB_PASSWORD` | Required |

Flyway applies database migrations at startup. Hibernate validates entity mappings against the database schema.

**Base URL:** `http://localhost:8080`

## API Endpoints

| Method | Endpoint | Success response |
| --- | --- | --- |
| `GET` | `/api/health` | `200 OK` |
| `POST` | `/api/applications` | `201 Created` |
| `GET` | `/api/applications` | `200 OK` |
| `GET` | `/api/applications/{id}` | `200 OK` |
| `PATCH` | `/api/applications/{id}/status` | `200 OK` |
| `DELETE` | `/api/applications/{id}` | `204 No Content` |

Send request bodies with `Content-Type: application/json`.

### Create an application

```http
POST /api/applications
```

```json
{
  "company": "Atlassian",
  "role": "Software Engineer"
}
```

Both fields are required, must not be blank, and allow up to 100 characters.

Example response:

```json
{
  "id": 1,
  "company": "Atlassian",
  "role": "Software Engineer",
  "status": "SAVED"
}
```

The database generates the ID. New applications start with `SAVED`.

### Update status

```http
PATCH /api/applications/1/status
```

```json
{
  "status": "APPLIED"
}
```

Use one of the supported uppercase status values.

### Filter applications by status

```http
GET /api/applications?status=APPLIED
```

The optional `status` parameter filters applications by status.
Omit it to retrieve all applications. A valid status with no matches
returns an empty list.

Supported values: `SAVED`, `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED`.


### Error responses

Validation failure — `400 Bad Request`:

```json
{
  "company": "Company is required"
}
```

Missing application — `404 Not Found`:

```json
{
  "message": "Application not found with id: 999"
}
```

Deleting an application returns an empty response body. Deleting a missing application returns `404`.

## Testing

Ensure Docker is running, then execute:

```powershell
.\mvnw.cmd clean verify
```

The current suite includes:

- **Two unit tests:** verify service deletion behavior using Mockito.
- **One integration test:** verifies creation, retrieval, status updates, and deletion against real PostgreSQL.

Testcontainers provides an isolated temporary database. Flyway applies migrations to that database before the integration test runs. Development data is not used.

HTTP responses and restart persistence have been verified manually with Postman; these checks are not yet automated.

## Project Structure

| Package / directory | Responsibility |
| --- | --- |
| `controller` | REST endpoints |
| `dto` | Request objects and validation |
| `service` | Business logic and transactions |
| `repository` | Database access |
| `model` | JPA entity and status enum |
| `exception` | Exceptions and HTTP error handling |
| `src/main/resources/db/migration` | Versioned SQL migrations |
| `src/test/java` | Unit and integration tests |

## Development Notes

- Commit `.env.example` with placeholders; never commit credentials.
- Add a new Flyway migration for schema changes rather than editing an applied migration.
- Run `.\mvnw.cmd clean verify` before opening a pull request.
- Database initialization settings apply to a fresh volume. Editing `.env` does not change an existing database password.

## Planned

- Search and pagination
- User authentication and application ownership
- React frontend
- CI and deployment