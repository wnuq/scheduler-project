# Task 5: Admin Features

**Goal:** Implement specific views and actions for the Administrator role.

## 1. Access Control
- Ensure admin endpoints are protected (require `ADMIN` role logic).

## 2. Admin Dashboard (`/admin/dashboard`)
- **View:** List of all registered students.
- **Columns:** Name, Email, Remaining Hours, Total Bookings.
- **Action:** (Optional) Edit remaining hours (e.g., top up).

## 3. Enhanced Calendar View (for Admin)
- Admin sees the same grid as students, BUT:
    - `TAKEN` slots show the Name of the student.
    - `FREE` slots can be clicked to `BLOCK`.
    - `BLOCKED` slots can be clicked to `UNBLOCK`.
    - `BOOKED` slots can be clicked to `CANCEL` (Admin override).

## 4. Admin Controller (`AdminController`)
- `POST /admin/block`: Blocks a slot.
- `POST /admin/unblock`: Removes a block.
- `POST /admin/student/update-hours`: Updates a student's balance.

## Acceptance Criteria
- [ ] Admin can see who booked which slot.
- [ ] Admin can block an empty slot (turns Gray for everyone).
- [ ] Admin can see the list of students and their balances.
