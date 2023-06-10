package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.ReviewResponse;
import com.kg.platform.online_course.models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "fullName", expression = "java(review.getUser().getName() + \" \" + review.getUser().getSurname())")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(source = "fullName", target = "user.name")
    @Mapping(expression = "java(\"\")", target = "user.surname")
    Review toReview(ReviewResponse reviewDTO);

}
