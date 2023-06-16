package com.kg.platform.online_course.services;

import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.models.Video;
import com.kg.platform.online_course.repositories.LessonRepository;
import com.kg.platform.online_course.repositories.VideoRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class VideoService {
    private static final String UPLOAD_DIR = "./uploads"; // Directory to store uploaded videos
    private VideoRepository videoRepository;
    private LessonRepository lessonRepository;

    @PostConstruct
    public void init() {
        if (!Path.of(UPLOAD_DIR).toFile().exists()) {
                new File(UPLOAD_DIR).mkdir();
        }
    }
    @Async
    public void saveVideo(String courseName, Long lessonId, MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Save the video file to the upload directory
        String uploadPath = UPLOAD_DIR + "/" +courseName;
        File dir = new File(uploadPath);

        if (!dir.exists())
            dir.mkdir();

        Path path = Path.of(uploadPath + "/" +  filename);
        file.transferTo(path);

        // Create a new video entity and save it
        Lesson lesson = lessonRepository.findById(lessonId).get();

        Video video = new Video();
        video.setPath(path.toString());

        lesson.setVideo(video);
        lessonRepository.save(lesson);

    }

    @SneakyThrows
    public void deleteVideoById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        try {
            Files.delete(Path.of(video.getPath()));
        } catch (IOException e) {
            throw new FileSystemException("Something wrong happened , file can't be deleted");
        }
        videoRepository.delete(video);

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
