package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findByCourseId(Long course);
}
