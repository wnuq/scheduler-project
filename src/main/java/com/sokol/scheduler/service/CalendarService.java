package com.sokol.scheduler.service;

import com.sokol.scheduler.domain.Lesson;
import com.sokol.scheduler.domain.LessonStatus;
import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.domain.dto.SlotStatus;
import com.sokol.scheduler.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final LessonRepository lessonRepository;

    private static final int START_HOUR = 8;
    private static final int END_HOUR = 20;

    @Transactional(readOnly = true)
    public Map<LocalDateTime, SlotStatus> getWeeklySchedule(LocalDate startOfWeek, User currentUser) {
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = startOfWeek.plusDays(6).atTime(23, 59, 59);

        // 1. Fetch all lessons for the week
        List<Lesson> lessons = lessonRepository.findAllByStartTimeBetween(startDateTime, endDateTime);

        // 2. Map occupied slots
        Map<LocalDateTime, Lesson> lessonMap = lessons.stream()
                .collect(Collectors.toMap(Lesson::getStartTime, l -> l));

        // 3. Generate grid and determine status
        Map<LocalDateTime, SlotStatus> schedule = new HashMap<>();

        for (int day = 0; day < 7; day++) {
            LocalDate date = startOfWeek.plusDays(day);
            for (int hour = START_HOUR; hour < END_HOUR; hour++) {
                LocalDateTime slotTime = date.atTime(hour, 0);
                
                // Determine status
                SlotStatus status = determineStatus(slotTime, lessonMap.get(slotTime), currentUser);
                schedule.put(slotTime, status);
            }
        }
        return schedule;
    }

    private SlotStatus determineStatus(LocalDateTime slotTime, Lesson lesson, User currentUser) {
        if (slotTime.isBefore(LocalDateTime.now())) {
            return SlotStatus.PAST;
        }

        if (lesson == null) {
            return SlotStatus.FREE;
        }

        if (lesson.getStatus() == LessonStatus.BLOCKED) {
            return SlotStatus.BLOCKED;
        }

        if (lesson.getStudent() != null && lesson.getStudent().getId().equals(currentUser.getId())) {
            return SlotStatus.MY_BOOKING;
        }

        return SlotStatus.TAKEN;
    }
}
