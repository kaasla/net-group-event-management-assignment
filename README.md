# Event Management Application

## Tech Stack

- **Frontend:** React 19, TypeScript 5.9, Tailwind CSS 4, Vite 7
- **Backend:** Java 21, Spring Boot 4.0.2, Spring Security + JWT, Maven
- **Database:** PostgreSQL 17
- **Testing:** JUnit 5 + Mockito
- **Linting:** ESLint + Prettier (frontend), Checkstyle (backend)
- **Infra:** Docker, Nginx, Flyway

## Admin Credentials

```
Email:    admin@eventmanagement.com
Password: admin123
```

## API Documentation

Full API documentation is available via Swagger UI at http://localhost:8080/swagger-ui.html when the backend is running.

## Validation

- **Estonian personal code** — 11-digit code validated with checksum verification. Use [ID code generator](https://dknight.github.io/Isikukood-js/) to generate valid codes for testing
- **Event capacity** — registration is rejected when an event reaches its max participants
- **Duplicate registration** — a person cannot register for the same event twice (enforced by unique constraint on event_id + personal_code)

## Prerequisites

- Docker
- Java 21 and Maven (for local backend development)
- Node.js 22 and pnpm (for local frontend development)

## Running with Docker Compose

Start all services (PostgreSQL, backend, frontend) with a single command:

```bash
docker compose up --build
```

The application will be available at:
- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080/api/v1
- **Swagger UI:** http://localhost:8080/swagger-ui.html

To stop and clean up:

```bash
docker compose down -v
```

## Running Locally

### 1. Start PostgreSQL

```bash
docker compose up postgres
```

### 2. Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

### 3. Start the frontend

```bash
cd frontend
pnpm install
pnpm dev
```

The frontend dev server runs at http://localhost:5173

## Database

The database schema is managed by Flyway migrations located in `backend/src/main/resources/db/migration/`. Migrations run automatically when the backend starts — no manual setup is needed beyond starting PostgreSQL.

## Running Tests

```bash
cd backend
./mvnw test
```
