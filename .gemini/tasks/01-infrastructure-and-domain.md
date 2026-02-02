# Task 1: Infrastructure & Domain Layer

**Goal:** Initialize the project structure, dependencies, and core database entities required for the Driving School Calendar.

## 1. Project Dependencies (`pom.xml`)
Ensure the following dependencies are present (Spring Boot 3.x):
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-web`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-resource-server` (For Clerk JWT handling)
- `h2` (or your preferred database driver)
- `lombok` (optional, for boilerplate)

## 2. Application Configuration (`application.properties`)
- Configure Database connection (H2 console enabled for dev).
- Placeholder configuration for Clerk (Issuer URI, JWK Set URI).

## 3. Domain Entities
Create the Hibernate/JPA entities based on `urs.md` Section 6.

### Entity: `User`
*   **Table:** `app_user` (avoid reserved word 'user')
*   **Fields:**
    *   `id` (Long, PK)
    *   `clerkId` (String, Unique, Index) - To map external identity.
    *   `email` (String)
    *   `firstName` (String)
    *   `lastName` (String)
    *   `role` (Enum: `STUDENT`, `ADMIN`)
    *   `remainingHours` (Integer) - Default: 30.

### Entity: `Lesson`
*   **Table:** `lesson`
*   **Fields:**
    *   `id` (Long, PK)
    *   `startTime` (LocalDateTime) - Represents the slot start.
    *   `student` (ManyToOne relationship to `User`, nullable).
    *   `status` (Enum: `AVAILABLE`, `BOOKED`, `BLOCKED`) - *Optimization: If a record exists, it's not 'AVAILABLE'. Availability is the absence of a Lesson record, or we pre-generate slots. Recommendation: Only store booked/blocked slots.*
        *   *Refined Approach:* Store `startTime` and `status`. If `student` is null and `status` is `BLOCKED`, it's an admin block. If `student` is set, it's a booking.

## 4. Repositories
- `UserRepository` (findByClerkId, findByEmail)
- `LessonRepository` (findAllByStartTimeBetween - for fetching calendar ranges)

## Acceptance Criteria
- [ ] Project builds successfully with `mvn clean install`.
- [ ] Database schema is generated automatically by Hibernate.
- [ ] H2 Console is accessible (if using H2).
