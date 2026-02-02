package com.sokol.scheduler.service;

import com.sokol.scheduler.domain.Lesson;
import com.sokol.scheduler.domain.LessonStatus;
import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.repository.LessonRepository;
import com.sokol.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public void bookSlot(User student, LocalDateTime startTime) {
        // Validation: Past date
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nie można rezerwować terminów w przeszłości.");
        }

        // BR-001: Valid hour (simplified check, assumed enforced by UI/Controller, but good to have)
        if (startTime.getMinute() != 0 || startTime.getSecond() != 0) {
            throw new IllegalArgumentException("Lekcje zaczynają się tylko o pełnych godzinach.");
        }

        // BR-004: Slot occupied
        boolean slotOccupied = lessonRepository.findAllByStartTimeBetween(startTime, startTime).stream().findFirst().isPresent();
        // findAllByStartTimeBetween is used because exact match might be tricky with nanos, but between(time, time) works for equality usually or use specific query.
        // Actually, let's trust the logic: if a record exists at this time, it's booked/blocked.
        // Better: countByStartTime
        // For now, relying on list fetch which is fine for MVP.
        if (!lessonRepository.findAllByStartTimeBetween(startTime, startTime).isEmpty()) {
             throw new IllegalStateException("Ten termin jest już zajęty.");
        }

        // BR-003: Remaining hours limit
        if (student.getRemainingHours() <= 0) {
            throw new IllegalStateException("Wykorzystałeś już swój pakiet 30 godzin.");
        }

        // BR-002: Daily limit (Max 1 per day)
        LocalDateTime startOfDay = startTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startTime.toLocalDate().atTime(LocalTime.MAX);
        int lessonsToday = lessonRepository.countByStudentAndStartTimeBetween(student, startOfDay, endOfDay);
        if (lessonsToday >= 1) {
            throw new IllegalStateException("Możesz zarezerwować maksymalnie 1 godzinę dziennie.");
        }

        // Create booking
        Lesson lesson = new Lesson();
        lesson.setStartTime(startTime);
        lesson.setStudent(student);
        lesson.setStatus(LessonStatus.BOOKED);
        lessonRepository.save(lesson);

        // Decrement hours
        student.setRemainingHours(student.getRemainingHours() - 1);
        userRepository.save(student);
    }
}
