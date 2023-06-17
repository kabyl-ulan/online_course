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
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class VideoService {

    private VideoRepository videoRepository;
    private LessonRepository lessonRepository;
    private final String UPLOAD_DIR="./uploads";

    private Set<String> existedCourse = new HashSet<>();
    @PostConstruct
    public void init() {
        if (!Path.of(UPLOAD_DIR).toFile().exists()) {
                new File(UPLOAD_DIR).mkdir();
        }
    }

    @SneakyThrows
    @Async
     public void saveVideo(String courseName, Long lessonId, MultipartFile file)  {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (!existedCourse.contains(courseName))
            createDirectory(courseName);

        Path path = Path.of(   UPLOAD_DIR + "/" +courseName + "/" + filename);
        file.transferTo(path);

        // Create a new video entity and save it
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(NotFoundException::new);

        Video video = new Video();
        video.setPath(path.toAbsolutePath().toString());

        lesson.setVideo(video);


        lessonRepository.save(lesson);

    }
    

    private void createDirectory(String courseName) {
        String uploadPath = UPLOAD_DIR + "/" + courseName;
        File dir = new File(uploadPath);

        if (!dir.exists()) {
            dir.mkdir();
            existedCourse.add(courseName);
        }
    }


    @SneakyThrows
    public void deleteVideoById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        deleteFromFs(video.getPath());
        videoRepository.delete(video);
    }

    private void deleteFromFs(String path) throws FileSystemException {
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            throw new FileSystemException("Something wrong happened , file can't be deleted");
        }
    }

    @SneakyThrows
    public void updateVideo(Lesson lesson,String courseName, MultipartFile file) {
        Video video = lesson.getVideo();
        deleteFromFs(video.getPath());
        saveVideo(courseName,lesson.getId(),file);

    }
}
