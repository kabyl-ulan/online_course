package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonRequest {
    private String title;
    private Long courseId;
    private MultipartFile file;

}
