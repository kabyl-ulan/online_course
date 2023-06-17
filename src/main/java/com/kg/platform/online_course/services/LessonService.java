package com.kg.platform.online_course.services;

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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LessonService {
    private LessonRepository lessonRepository;
    private LessonsMapper lessonsMapper;
    private CourseRepository courseRepository;
    private VideoService videoService;

    public LessonResponse uploadLesson(Long courseId, String title, MultipartFile file){

        Course course = courseRepository.findById(courseId).get();

        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setCourse(course);

        course.getLessons().add(lesson);

        lessonRepository.saveAndFlush(lesson);
        courseRepository.save(course);

        videoService.saveVideo(course.getCourseName(),lesson.getId(),file);

        return lessonsMapper.toLessonResponse(lesson);
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(NotFoundException::new);
        return lessonsMapper.toLessonResponse(lesson);
    }

    public List<LessonResponse> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId)
                .stream()
                .map(lessonsMapper::toLessonResponse)
                .toList();
    }

    public void deleteById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(NotFoundException::new);
        videoService.deleteVideoById(lesson.getVideo().getId());
        lessonRepository.delete(lesson);
    }

    public LessonResponse updateById(Long lessonId, String title, MultipartFile file) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(NotFoundException::new);
        lesson.setTitle(title);
        if (file != null && !file.isEmpty()) {
            String courseName = lesson.getCourse().getCourseName();
            videoService.updateVideo(lesson,courseName,file);
        }
        lessonRepository.save(lesson);

        return lessonsMapper.toLessonResponse(lesson);
    }
}
