package com.kg.platform.online_course.api_controller;

import com.kg.platform.online_course.models.Video;
import com.kg.platform.online_course.services.VideoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Video API", description = "The Video API for all ")
public class VideoController {

    private VideoService videoService;

    @Async
    @PostMapping("/upload")
    public void uploadVideo(String courseName,Long lessonId,@RequestParam("file") MultipartFile file) {

        try {
            // Save the video file
            videoService.saveVideo(courseName,lessonId,file);

        } catch (IOException e) {
            throw new RuntimeException("The video can't be downloaded!");
        }
    }

    @GetMapping("/{id}")
    public String getVideoById(@PathVariable("id") Long id) {
        // Get the video by ID
        Video video = videoService.getVideoById(id);

        return video != null ? video.getPath() : "There is no video";

    }
    @GetMapping
    public List<String> getAllVideoNames() {
        // Retrieve all video names
        return videoService.getAllVideoNames();
    }
}
