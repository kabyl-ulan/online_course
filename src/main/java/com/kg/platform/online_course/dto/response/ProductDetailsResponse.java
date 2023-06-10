package com.kg.platform.online_course.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponse {
    private long id;
    private int price;
    private int amount;
    private int sold;

    private String category;
    private String productName;
    private String description;

    private LocalDate receiptDate;

    private List<ImageDto> imageList;
    private Set<ReviewResponse> reviews;

}
