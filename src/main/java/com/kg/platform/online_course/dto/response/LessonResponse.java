package com.kg.platform.online_course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonResponse {
    private Long id;
    private String title;
    private String videoPath;
}
