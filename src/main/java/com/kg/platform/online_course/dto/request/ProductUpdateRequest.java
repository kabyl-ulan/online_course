package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductUpdateRequest {
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer amount;
}
