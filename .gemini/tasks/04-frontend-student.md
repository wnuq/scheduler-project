# Task 4: Frontend Implementation (Thymeleaf)

**Goal:** Create the user interface for the schedule and booking interaction.

## 1. Layout & Styling
- Create `src/main/resources/templates/layout/main.html` (Base layout).
- Integrate a CSS framework (Bootstrap 5 or Tailwind via CDN) for responsiveness (**NFR-001**).
- Add Clerk JS SDK for frontend session management (Logout button, User Profile).

## 2. Schedule Controller (`ScheduleController`)
- **Endpoint:** `GET /schedule`
- **Model:**
    - `weekStart`: Date of the current week view.
    - `slots`: The Grid DTO from `CalendarService`.
    - `userBalance`: Current user's remaining hours.
    - `messages`: Flash attributes for success/error messages.

## 3. Calendar View (`schedule.html`)
- Display a table/grid.
- **Header:** Mon - Sun dates.
- **Rows:** Hours (e.g., 08:00 - 18:00).
- **Cells:**
    - If `FREE` (Green): Button/Form to POST `/schedule/book`.
    - If `MY_BOOKING` (Blue): Display "My Lesson".
    - If `TAKEN` (Gray): Display "Unavailable".
    - If `BLOCKED` (Gray): Display "Unavailable".
- **Navigation:** Buttons to go to Previous/Next Week.

## 4. Interaction
- Implement `POST /schedule/book` endpoint.
    - Params: `dateTime`.
    - Calls `BookingService.bookSlot`.
    - Redirects back to `/schedule` with success/error message.

## Acceptance Criteria
- [ ] User can view the weekly calendar.
- [ ] User sees their own bookings in Blue.
- [ ] User sees available slots in Green.
- [ ] Clicking a Green slot successfully books it and refreshes the page.
- [ ] Error messages (e.g., "Daily limit reached") are displayed to the user.
