package com.kg.platform.online_course.api_controller;

import com.kg.platform.online_course.models.Video;
import com.kg.platform.online_course.services.VideoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/videos")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Video API", description = "The Video API for all ")
public class VideoController {
    private static final String UPLOAD_DIR = "./uploads"; // Directory to store uploaded videos


    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Save the video file
            videoService.saveVideo(filename, file);

            return ResponseEntity.ok("Video uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable("id") Long id) {
        // Get the video by ID
        Video video = videoService.getVideoById(id);

        if (video != null) {
            try {
                // Construct the file path
                Path filePath = Paths.get(UPLOAD_DIR).resolve(video.getFilename()).normalize();
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists() && resource.isReadable()) {
                    return ResponseEntity.ok().body(resource);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllVideoNames() {
        // Retrieve all video names
        List<String> videoNames = videoService.getAllVideoNames();

        return ResponseEntity.ok(videoNames);
    }
}
