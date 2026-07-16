# Task Manager API

A REST API for managing users, projects and tasks.

Version 1 provides basic CRUD operations using a layered Spring Boot architecture and PostgreSQL persistence.

## What it does

The application manages three main entities:

- User
- Project
- Task

A user can have many tasks.
A project can have many tasks.
Each task belongs to one user and one project.

## Features

- CRUD operations for users
- CRUD operations for projects
- CRUD operations for tasks
- Relations between tasks, users and projects
- PostgreSQL persistence
- Manual API testing with Postman

## Technologies

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- Postman

## Architecture

```text
HTTP request
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Hibernate / JPA
    ↓
PostgreSQL
```

## Main endpoints

### Users

```http
POST   /users
GET    /users
GET    /users/{id}
PUT    /users/{id}
DELETE /users/{id}
```

### Projects

```http
POST   /projects
GET    /projects
GET    /projects/{id}
PUT    /projects/{id}
DELETE /projects/{id}
```

### Tasks

```http
POST   /tasks
GET    /tasks
GET    /tasks/{id}
PUT    /tasks/{id}
DELETE /tasks/{id}
```

## Example request bodies

### Create User

```json
{
  "name": "Jakub",
  "email": "jakub.kowalski@example.com"
}
```

The Postman collection generates a unique test email automatically.

### Create Project

```json
{
  "name": "Website Redesign",
  "status": "Planned",
  "deadline": "2026-12-01"
}
```

### Create Task

A task requires an existing user and an existing project.

First create a user and a project. Then use the returned IDs when creating a task.

The values below are example IDs and should be replaced with IDs of existing records.

```json
{
  "title": "Write documentation",
  "description": "Prepare basic API documentation",
  "status": "Planned",
  "deadline": "2026-11-15",
  "assignedUser": {
    "id": 1
  },
  "project": {
    "id": 1
  }
}
```

### Update Task

The task ID is provided in the request URL:

`PUT /tasks/{id}`

The user and project IDs in the request body must refer to existing records.

```json
{
  "title": "Update documentation",
  "description": "Add more details to API documentation",
  "status": "In Progress",
  "deadline": "2026-12-01",
  "assignedUser": {
    "id": 1
  },
  "project": {
    "id": 1
  }
}
```

## API testing with Postman

The API was tested manually with Postman.

The Postman collection will be added in the `postman/` folder.

### Dynamic test email

Before the `POST /users` request is sent, a Postman pre-request script generates a unique email address:

```javascript
const uniqueEmail = `jakub.kowalski.${Date.now()}@example.com`;

pm.collectionVariables.set("userEmail", uniqueEmail);
```

The current timestamp makes the email unique for each test run.

The generated value is stored in the `userEmail` collection variable and used in the request body:

```json
{
  "name": "Jakub",
  "email": "{{userEmail}}"
}
```

This prevents duplicate-email conflicts when the collection is run multiple times.

Recommended test order:

| Step | Operation | Request | Expected status | Expected result |
|---:|---|---|---|---|
| 01 | Create User | `POST /users` | `200 OK` | A user is created and `userId` is stored |
| 02 | Create Project | `POST /projects` | `200 OK` | A project is created and `projectId` is stored |
| 03 | Create Task | `POST /tasks` | `200 OK` | A task is created and `taskId` is stored |
| 04 | Get All Tasks | `GET /tasks` | `200 OK` | The list of tasks is returned |
| 05 | Get Task By ID | `GET /tasks/{{taskId}}` | `200 OK` | The created task is returned |
| 06 | Update Task | `PUT /tasks/{{taskId}}` | `200 OK` | The task is updated |
| 07 | Get Updated Task | `GET /tasks/{{taskId}}` | `200 OK` | The updated task is returned |
| 08 | Delete Task | `DELETE /tasks/{{taskId}}` | `200 OK` | The task is deleted |
| 09 | Verify Deleted Task | `GET /tasks/{{taskId}}` | `404 Not Found` | The deleted task is no longer available |

## How to run locally

### Prerequisites

- Java 21
- PostgreSQL

### Create the database

```sql
CREATE DATABASE taskmanager_db;
```

### Configure environment variables

The application reads database credentials from environment variables.

Required environment variables:

```text
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
```

Optional variable:

```text
DB_URL=jdbc:postgresql://localhost:5432/taskmanager_db
```

The default database URL is:

```text
jdbc:postgresql://localhost:5432/taskmanager_db
```

You can configure these variables in your operating system or in the IntelliJ IDEA Run Configuration.

### Run on Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Run on Linux or macOS

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Current limitations

Version 1 uses entity objects directly in HTTP requests and responses.

API testing is currently performed manually with Postman.

DTOs, request validation, global exception handling and automated tests are not included in this version.

## Next steps

- DTO layer
- Bean Validation
- Global exception handling
- Automated tests
