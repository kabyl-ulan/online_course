package com.kg.platform.online_course.services;

import com.kg.platform.online_course.dto.request.LessonRequest;
import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.mappers.LessonsMapper;
import com.kg.platform.online_course.models.Course;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.repositories.CourseRepository;
import com.kg.platform.online_course.repositories.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class LessonService {
    private LessonRepository repository;
    private LessonsMapper lessonsMapper;
    private CourseRepository courseRepository;
    public void uploadLesson(LessonRequest request){

        Course course = courseRepository.findById(request.getCourseId()).get();
        Lesson lesson = lessonsMapper.toLesson(request);
        course.getLessons().add(lesson);

        courseRepository.save(course);
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = repository.findById(id).orElseThrow(NotFoundException::new);
        return lessonsMapper.toLessonResponse(lesson);
    }

    public List<LessonResponse> getLessonsByCourseId(Long courseId) {
        return repository.findByCourseId(courseId)
                .stream()
                .map(lessonsMapper::toLessonResponse)
                .toList();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
