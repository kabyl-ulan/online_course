package com.kg.platform.online_course.services;

import com.kg.platform.online_course.models.Video;
import com.kg.platform.online_course.repositories.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class VideoService {
    private static final String UPLOAD_DIR = "./uploads"; // Directory to store uploaded videos
    private VideoRepository videoRepository;

    @PostConstruct
    public void init() {
        if (!Path.of(UPLOAD_DIR).toFile().exists()) {
                new File(UPLOAD_DIR).mkdir();
        }
    }
    public void saveVideo(String courseName,String filename, MultipartFile file) throws IOException {
        // Save the video file to the upload directory
        String uploadPath = UPLOAD_DIR + "/" +courseName;
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdir();

        Path path = Path.of(uploadPath + "/" +  filename);
        file.transferTo(path);

        // Create a new video entity and save it
        Video video = new Video();
        video.setPath(path.toString());
        videoRepository.save(video);
    }

    public Video getVideoById(Long id) {
        // Retrieve video by ID from the repository
        return videoRepository.findById(id).orElse(null);
    }

    public List<String> getAllVideoNames() {
        // Retrieve all video names from the repository
        List<Video> videos = videoRepository.findAll();
        return videos.stream().map(Video::getPath).toList();
    }
}
