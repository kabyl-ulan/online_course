package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewRequest {
    private Long productId;
    private String text;
}
