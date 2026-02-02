package com.sokol.scheduler.service;

import com.sokol.scheduler.domain.Lesson;
import com.sokol.scheduler.domain.LessonStatus;
import com.sokol.scheduler.domain.Role;
import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.domain.dto.StudentDTO;
import com.sokol.scheduler.repository.LessonRepository;
import com.sokol.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void unblockSlot(LocalDateTime startTime) {
        lessonRepository.findAllByStartTimeBetween(startTime, startTime).stream()
                .filter(l -> l.getStatus() == LessonStatus.BLOCKED)
                .findFirst()
                .ifPresent(lessonRepository::delete);
    }

    @Transactional
    public void cancelLesson(LocalDateTime startTime) {
        lessonRepository.findAllByStartTimeBetween(startTime, startTime).stream()
                .filter(l -> l.getStatus() == LessonStatus.BOOKED)
                .findFirst()
                .ifPresent(lesson -> {
                    // Refund hours if a student was assigned
                    if (lesson.getStudent() != null) {
                        User student = lesson.getStudent();
                        student.setRemainingHours(student.getRemainingHours() + 1);
                        userRepository.save(student);
                    }
                    lessonRepository.delete(lesson);
                });
    }

    @Transactional
    public void updateStudentHours(Long studentId, int newHours) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono studenta."));
        student.setRemainingHours(newHours);
        userRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return userRepository.findAllByRole(Role.STUDENT).stream()
                .map(u -> new StudentDTO(
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        u.getRemainingHours(),
                        lessonRepository.countByStudent(u)
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Lesson> getLessonsForRange(LocalDateTime start, LocalDateTime end) {
        return lessonRepository.findAllByStartTimeBetween(start, end);
    }
}
