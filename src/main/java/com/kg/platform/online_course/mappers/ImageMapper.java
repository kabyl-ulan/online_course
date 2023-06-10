package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.ImageDto;
import com.kg.platform.online_course.models.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageMapper {

	ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(source = "imageData", target = "data")
	ImageDto toImageDto(Image image);
}
