# JobTracker

A Java/Spring Boot application for tracking job applications.
Currently under development.

## Current features

- Create and list job applications
- Retrieve an application by ID
- Update application status
- Validate request data
- Return field validation errors and application-not-found responses

## Stack

- Java
- Spring Boot / Spring MVC
- Jakarta Bean Validation
- Maven

## Run locally

Use a JDK compatible with the Java version configured in pom.xml.

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS / Linux:

```bash
./mvnw spring-boot:run
```

Default base URL: http://localhost:8080

## API endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | /api/health | Check application availability |
| POST | /api/applications | Create an application |
| GET | /api/applications | List applications |
| GET | /api/applications/{id} | Retrieve one application |
| PATCH | /api/applications/{id}/status | Update status |
| DELETE | /api/applications/{id} | Delete an application |

Create request:

```json
{
  "company": "Atlassian",
  "role": "Software Engineer"
}
```

Status update request:

```json
{
  "status": "APPLIED"
}
```

Allowed statuses: SAVED, APPLIED, INTERVIEW, OFFER, REJECTED.

## Build and test

Windows:

```powershell
.\mvnw.cmd clean verify
```

macOS / Linux:

```bash
./mvnw clean verify
```

## Current limitations

- Data is stored in memory and is lost on restart.
- Authentication and user-specific access are not implemented.
- Frontend and database integration are pending.
- Automated API behavior tests are pending.