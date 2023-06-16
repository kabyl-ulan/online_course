package com.kg.platform.online_course.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailsResponse {
    private long id;
    private int price;
    private String category;
    private String courseName;
    private String description;
    private LocalDate uploadDate;
    private ImageDto courseImage;
    private List<LessonResponse> lessons;


}
