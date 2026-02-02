# GEMINI.md - Driving School Scheduler Context

## Project Overview

**Name:** Scheduler (Driving School Calendar)
**Description:** A web-based application for scheduling driving lessons. It allows students to book 1-hour slots (max 1/day, 30h limit) and administrators to manage availability and users.
**Technology Stack:**
*   **Language:** Java 21
*   **Framework:** Spring Boot 4.0.2
*   **Build Tool:** Maven
*   **Database:** H2 (Planned) / JPA (Hibernate)
*   **Frontend:** Thymeleaf (Server-Side Rendering)
*   **Authentication:** Clerk (Planned integration)

## Current Status & Architecture

The project is currently in the **scaffolding phase**.
*   **Configuration:** The `pom.xml` includes core Web and JPA starters but is missing planned dependencies for H2, Thymeleaf, and Security.
*   **Application Properties:** Minimal configuration; DB connection strings are missing.
*   **Codebase:** Contains basic `SchedulerApplication` entry point. Domain entities and controllers are not yet implemented.

### Key Documentation (in `.gemini/`)
*   `urs.md`: **User Requirement Specifications**. The source of truth for features and rules (e.g., "Max 1 hour per day", "30 hours total limit").
*   `idea.md`: High-level concept and feature summary.
*   `tasks/`: Step-by-step implementation guide.
    *   `01-infrastructure-and-domain.md`: Current focus (Entities, DB setup).

## Building and Running

**Prerequisites:** Java 21+

### Build
```bash
./mvnw clean install
```

### Run
```bash
./mvnw spring-boot:run
```

### Tests
```bash
./mvnw test
```

## Development Conventions

*   **Package Structure:** `com.sokol.scheduler`
*   **Entity Naming:** Use `app_user` for the User table to avoid SQL reserved word conflicts.
*   **Code Style:** Standard Java/Spring conventions. Use Lombok for boilerplate (Getters/Setters/Constructors).
*   **Architectural Pattern:** Standard Layered Architecture (Controller -> Service -> Repository -> Domain).

## Immediate Next Steps (from Task 01)
1.  Update `pom.xml` with missing dependencies:
    *   `h2`
    *   `spring-boot-starter-thymeleaf`
    *   `spring-boot-starter-security`
    *   `spring-boot-starter-oauth2-resource-server`
2.  Configure `application.properties` for H2 and Clerk.
3.  Implement Domain Entities (`User`, `Lesson`) as defined in `01-infrastructure-and-domain.md`.
