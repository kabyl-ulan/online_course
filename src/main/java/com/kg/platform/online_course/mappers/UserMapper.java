package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.UserResponse;
import com.kg.platform.online_course.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "fullName",expression = "java(user.getSurname() + \" \" + user.getName())")
    UserResponse toUserResponse(User user);
}
