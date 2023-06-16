package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.CourseDetailsResponse;
import com.kg.platform.online_course.dto.response.ProductResponse;
import com.kg.platform.online_course.models.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ReviewMapper.class, ImageMapper.class,LessonsMapper.class})
public interface CourseMapper {
    @Mapping(target = "category", source = "category.categoryName")
    CourseDetailsResponse toCourseDetailsResponse(Course course);

    ProductResponse toProductResponse(Course course);



}

