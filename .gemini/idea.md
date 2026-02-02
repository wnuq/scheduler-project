# General System Requirements – Driving School Calendar

## 1. System Purpose
The system should allow students to register for driving lessons and manage their lesson schedule through a clear calendar interface, considering instructor availability and student hour limits. The system should also allow the administrator (instructor) to manage time block availability and monitor students' status.

---

## 2. Users
### 2.1 Student
- Can view available lessons.
- Can register for available lessons (maximum 1 hour per day).
- Has a limited number of hours (default 30h).
- Receives notifications when the available hours are running low.

### 2.2 Administrator (Instructor)
- Can block or unblock any time blocks in the calendar.
- Can view the schedules of their students.
- Can see how many hours each student has left.
- Can click on a lesson to see which student is registered for it.

---

## 3. System Features

### 3.1 Calendar / Schedule
- Main page `/schedule` displays a time grid:
    - **Columns:** days of the week.
    - **Rows:** hourly time blocks (1h each).
- Block colors:
    - **Green:** available for registration.
    - **Blue:** registered block (for the logged-in student).
    - **Gray:** unavailable block (taken by others or blocked by admin).

### 3.2 Lesson Registration
- Students can register only for available blocks (green).
- Maximum 1 hour per day.
- Initial student hour limit: 30h.
- The system tracks hours used by each student.

### 3.3 Administrator Functions
- Block/unblock selected hourly blocks.
- View student schedules.
- See the number of remaining hours for each student.
- View details of registered lessons (e.g., which student is assigned to a block).

### 3.4 Notifications
- The system can send notifications to students, e.g.:
    - Alert when remaining hours are low.
    - Reminders about upcoming lessons (optional).

---

## 4. Constraints / Business Rules
- One block = 1 hour.
- A student can register for a maximum of 1 block per day.
- Each student starts with 30 hours.
- Unavailable blocks (gray) cannot be booked by students.

---

## 5. Non-Functional Requirements
- Responsive system (desktop and mobile view).
- Intuitive interface – quick overview of availability and registrations.
- Support for multiple students registering simultaneously.
- Secure login and user identification (student / administrator) - maybe Clerk is a good option.
