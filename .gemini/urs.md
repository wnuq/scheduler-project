# User Requirement Specification (URS) - Driving School Calendar

## 1. Introduction
### 1.1 Purpose
The purpose of the **Driving School Calendar** system is to streamline the process of scheduling driving lessons. It allows students to self-register for available time slots while adhering to specific booking limits, and enables administrators (instructors) to manage availability and monitor student progress.

### 1.2 Scope
The system will be a web-based application offering a responsive interface for both desktop and mobile users. It encompasses user authentication, calendar visualization, booking logic, and balance tracking.

---

## 2. User Roles
| Role | Description |
| :--- | :--- |
| **Student** | A learner driver who can view the schedule, book lessons, and track their remaining hours. |
| **Administrator (Instructor)** | The system manager who sets availability, views all bookings, and manages student accounts/hours. |

---

## 3. Functional Requirements (FR)

### 3.1 Authentication & Authorization
*   **FR-001:** The system shall allow users to log in using secure authentication (Candidate: Clerk).
*   **FR-002:** The system shall distinguish between Student and Administrator roles upon login.

### 3.2 Schedule Visualization (Calendar)
*   **FR-003:** The application shall display a calendar grid with columns representing days of the week and rows representing hourly time slots.
*   **FR-004:** Time slots must be color-coded based on status:
    *   **Green:** Available for registration.
    *   **Blue:** Currently booked by the logged-in student.
    *   **Gray:** Unavailable (booked by another student, blocked by admin, or past time).

### 3.3 Student Operations
*   **FR-005:** Students shall be able to book an available (Green) time slot.
*   **FR-006:** Upon booking, the student's remaining hour balance shall decrease by 1 hour.
*   **FR-007:** Students shall be able to view their remaining hour balance.
*   **FR-008:** Students shall receive a notification/warning when their hour balance is low.

### 3.4 Administrator Operations
*   **FR-009:** Administrators shall be able to block specific time slots (making them unavailable to students).
*   **FR-010:** Administrators shall be able to unblock time slots.
*   **FR-011:** Administrators shall be able to view the full schedule, including the identity of students booked in specific slots.
*   **FR-012:** Administrators shall have a dashboard to view a list of all students and their remaining hour balances.

### 3.5 Notifications
*   **FR-013:** The system shall trigger notifications for low hour balances.
*   **FR-014:** (Optional) The system shall send reminders for upcoming scheduled lessons.

---

## 4. Business Rules (BR)

*   **BR-001 (Slot Duration):** All driving lesson slots are fixed at 1 hour in duration.
*   **BR-002 (Daily Limit):** A student may register for a maximum of **1 hour per day**.
*   **BR-003 (Total Limit):** Each student is assigned a default package of **30 hours**. Once this limit is reached, they cannot book further lessons without Admin intervention.
*   **BR-004 (Exclusivity):** A time slot booked by one student becomes unavailable (Gray) to all other students.
*   **BR-005 (Admin Override):** Administrators can manage slots regardless of the standard booking rules (e.g., blocking a day for holidays).

---

## 5. Non-Functional Requirements (NFR)

### 5.1 Usability
*   **NFR-001:** The user interface must be responsive, functioning correctly on desktop browsers and mobile devices.
*   **NFR-002:** The status of calendar slots (Green/Blue/Gray) must be instantly recognizable without user action (e.g., no need to click to see status).

### 5.2 Performance
*   **NFR-003:** The calendar view should load availability data in near real-time to prevent double-booking issues during simultaneous access.

### 5.3 Security
*   **NFR-004:** Student data (remaining hours, schedule) must be private to that student and the Administrator. Students cannot see who has booked other slots (only that the slot is taken).

---

## 6. Data Entities (High Level)

### 6.1 User
*   ID, Name, Email, Role (Student/Admin), RemainingHours (int).

### 6.2 Appointment/Lesson
*   ID, StartTime, EndTime, StudentID (FK), Status (Booked/Blocked).

---

## 7. Future Considerations
*   Payment gateway integration for purchasing additional hours.
*   Instructor assignment (if expanding to multiple instructors).
