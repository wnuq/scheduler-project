package com.sokol.scheduler.service;

import com.sokol.scheduler.domain.Lesson;
import com.sokol.scheduler.domain.LessonStatus;
import com.sokol.scheduler.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final LessonRepository lessonRepository;

    @Transactional
    public void blockSlot(LocalDateTime startTime) {
         if (!lessonRepository.findAllByStartTimeBetween(startTime, startTime).isEmpty()) {
             throw new IllegalStateException("Ten termin jest już zajęty lub zablokowany.");
        }

        Lesson lesson = new Lesson();
        lesson.setStartTime(startTime);
        lesson.setStatus(LessonStatus.BLOCKED);
        lessonRepository.save(lesson);
    }
}
