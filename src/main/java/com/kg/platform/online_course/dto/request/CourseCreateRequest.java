package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseCreateRequest {
    private long categoryId;
    private String courseName;
    private String description;
    private double price;
    private String author;
}
