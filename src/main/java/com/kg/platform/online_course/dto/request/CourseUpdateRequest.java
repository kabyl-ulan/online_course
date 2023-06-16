package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseUpdateRequest {
    private Long productId;
    private String courseName;
    private String description;
    private Double price;
    private Integer amount;
}
