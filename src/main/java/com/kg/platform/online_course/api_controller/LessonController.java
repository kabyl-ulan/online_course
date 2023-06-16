package com.kg.platform.online_course.api_controller;

import com.kg.platform.online_course.dto.request.LessonRequest;
import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.services.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LessonController {
    private LessonService lessonService;
    @PostMapping
    public void upload (@RequestBody LessonRequest request) {
        lessonService.uploadLesson(request);
    }
    @GetMapping
    public LessonResponse getById(Long id) {
        return lessonService.getById(id);
    }

}
