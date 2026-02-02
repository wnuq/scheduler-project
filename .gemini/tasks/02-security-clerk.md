# Task 2: Security Integration (Clerk)

**Goal:** Secure the application using Clerk.com acting as an OIDC Identity Provider.

## 1. Security Configuration (`SecurityConfig.java`)
- Configure `SecurityFilterChain`.
- Set up **OAuth2 Resource Server** with JWT validation.
- Define public endpoints (e.g., static resources, login page if custom).
- Secure `/schedule/**` and API endpoints.

## 2. User Synchronization Service
*Clerk handles authentication, but we need the user in our local database for business logic (hours tracking).*
- Create a `UserService` method `syncUser(Jwt jwt)`.
- **Logic:**
    - Extract `sub` (Clerk ID) and email/name from the JWT claims.
    - Check if `User` exists in local DB by `clerkId`.
    - If not, create a new `User` with role `STUDENT` and default 30 hours.
    - If exists, update details if necessary.
- **Hook:** Implement an `AuthenticationSuccessHandler` or a Filter that runs after successful JWT validation to ensure the local user record exists before the controller is hit. Alternatively, check/sync in the `@Controller` via a `@ModelAttribute`.

## 3. Role Management
- Define how Admin roles are assigned.
- Option A: Manage roles in Clerk (Organization/Metadata) and map them to Spring Security Authorities.
- Option B: Manage roles locally in the `app_user` table. (Simpler for this scope: Just check the local DB entity for `role` = `ADMIN`).

## Acceptance Criteria
- [ ] Unauthenticated users are redirected to Clerk Login (or denied access).
- [ ] Authenticated requests contain a valid Principal.
- [ ] On first login, a new `User` record is created in the H2 database.
- [ ] `SecurityContextHolder` contains the user's details.
