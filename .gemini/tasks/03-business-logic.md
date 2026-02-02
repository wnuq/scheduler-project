# Task 3: Business Logic (Services)

**Goal:** Implement the core scheduling rules and validations described in the URS.

## 1. Calendar Service (`CalendarService`)
- **Method:** `getWeeklySchedule(LocalDate startOfWeek)`
- **Logic:**
    - Generate a grid of time slots (e.g., 8:00 to 20:00) for 7 days.
    - Query `LessonRepository` for existing bookings/blocks in that date range.
    - Map the results to a DTO (Data Transfer Object) structure optimized for the UI (e.g., `Map<LocalDateTime, SlotStatus>`).
    - **SlotStatus Enum for UI:** `FREE`, `MY_BOOKING`, `TAKEN`, `BLOCKED`.

## 2. Booking Service (`BookingService`)
- **Method:** `bookSlot(User student, LocalDateTime startTime)`
- **Validations (Exceptions):**
    - **BR-001:** Ensure slot is a valid hour (e.g., :00).
    - **BR-002:** Check if `LessonRepository` already has a record for `student` on `startTime.toLocalDate()` (Max 1 hr/day).
    - **BR-003:** Check if `student.getRemainingHours() > 0`.
    - **BR-004:** Check if slot is already occupied (exists in DB).
- **Action:**
    - Create and save `Lesson`.
    - Decrement `student.remainingHours`.
    - Save `User`.

## 3. Admin Service (`AdminService`)
- **Method:** `blockSlot(LocalDateTime startTime)`
    - Creates a `Lesson` with no student and status `BLOCKED`.
- **Method:** `unblockSlot(Long lessonId)`
    - Deletes the `Lesson` record (only if status is `BLOCKED` or if Admin is overriding a booking).
- **Method:** `getStudentStats()`
    - Returns list of students with their remaining hours.

## Acceptance Criteria
- [ ] Unit tests for `bookSlot` verify that a student cannot book >1 lesson per day.
- [ ] Unit tests verify that booking decrements hours.
- [ ] Unit tests verify that double booking fails.
