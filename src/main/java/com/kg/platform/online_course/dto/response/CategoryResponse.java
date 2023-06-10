package com.kg.platform.online_course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String categoryName;
    private ImageDto image;
}
