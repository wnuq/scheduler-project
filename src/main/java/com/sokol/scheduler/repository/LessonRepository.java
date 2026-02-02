package com.sokol.scheduler.repository;

import com.sokol.scheduler.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    int countByStudentAndStartTimeBetween(com.sokol.scheduler.domain.User student, LocalDateTime start, LocalDateTime end);
}
