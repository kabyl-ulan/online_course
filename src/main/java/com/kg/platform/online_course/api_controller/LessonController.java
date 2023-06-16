package com.kg.platform.online_course.api_controller;

import com.kg.platform.online_course.dto.request.LessonRequest;
import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.services.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/lesson")
public class LessonController {
    private LessonService lessonService;

    @Operation(summary = "Upload new lesson", description = "This endpoint upload new lesson to Course")
    @PostMapping()
    public void upload (@RequestBody LessonRequest request) {
        lessonService.uploadLesson(request);
    }

    @Operation(summary = "Get lesson by Id", description = "This endpoint return lesson by Id")
    @GetMapping("/{id}")
    public LessonResponse getById(@PathVariable Long id) {
        return lessonService.getById(id);
    }

    @Operation(summary = "Delete lesson by Id", description = "This endpoint delete lesson by Id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        lessonService.deleteById(id);
    }

}
