package com.kg.platform.online_course.services;

import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.models.Lesson;
import com.kg.platform.online_course.models.Video;
import com.kg.platform.online_course.repositories.LessonRepository;
import com.kg.platform.online_course.repositories.VideoRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
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

    private VideoRepository videoRepository;
    private LessonRepository lessonRepository;
    private final String UPLOAD_DIR="./uploads";

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
        // Save the video file to the upload directory
        String uploadPath = UPLOAD_DIR + "/" +courseName;


        Path path = Path.of(uploadPath + "/" +  filename);
        file.transferTo(path);

        // Create a new video entity and save it
        Lesson lesson = lessonRepository.findById(lessonId).get();

        Video video = new Video();
        Path fullPath = Path.of(uploadPath + "/" + filename).toAbsolutePath();

        video.setPath(fullPath.toString());
        video.setLesson(lesson);
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

//    public void updateVideo(Lesson lesson,MultipartFile file) {
//        Video video = lesson.getVideo();
//
//        deleteVideoById(lesson.getVideo().getId());
//
//
//    }
}
