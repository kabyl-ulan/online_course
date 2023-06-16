package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.request.LessonRequest;
import com.kg.platform.online_course.dto.response.LessonResponse;
import com.kg.platform.online_course.models.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonsMapper {
    @Mapping(source = "video.path",target = "videoPath")
    LessonResponse toLessonResponse(Lesson lesson);

    Lesson toLesson(LessonRequest request);
}
