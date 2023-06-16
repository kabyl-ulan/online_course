package com.kg.platform.online_course.api_controller;

import com.kg.platform.online_course.dto.request.LessonRequest;
import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.services.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/lesson")
public class LessonController {
    private LessonService lessonService;

    @Operation(summary = "Upload new lesson", description = "This endpoint upload new lesson to Course")
    @PostMapping("/upload")
    public void upload (Long courseId, String title,@RequestParam("file") MultipartFile file) {
        lessonService.uploadLesson(courseId,title,file);
    }

    @Operation(summary = "Get lesson by Id", description = "This endpoint return lesson by Id")
    @GetMapping("/{id}")
    public LessonResponse getById(@PathVariable Long id) {
        return lessonService.getById(id);
    }

    @Operation(summary = "Updte lesson by Id", description = "This endpoint return updated lesson by Id")
    @GetMapping("/{id}")
    public LessonResponse updateById(Long lessonId, String title,@RequestParam("file") MultipartFile file) {
        return lessonService.updateById(lessonId,title,file);
    }
    @Operation(summary = "Delete lesson by Id", description = "This endpoint delete lesson by Id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        lessonService.deleteById(id);
    }

}
