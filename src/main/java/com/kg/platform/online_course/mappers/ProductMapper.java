package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.ImageDto;
import com.kg.platform.online_course.dto.response.ProductDetailsResponse;
import com.kg.platform.online_course.dto.response.ProductResponse;
import com.kg.platform.online_course.models.Image;
import com.kg.platform.online_course.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ReviewMapper.class, ImageMapper.class})
public interface ProductMapper {
    @Mapping(target = "category", source = "category.categoryName")
    ProductDetailsResponse toProductDetailsResponse(Product product);

    @Mapping(target = "imageDto", source = "imageList")
    ProductResponse toProductResponse(Product product);

    default ImageDto toImageDto(List<Image> imageList) {
        return imageList != null && !imageList.isEmpty() ? ImageMapper.INSTANCE.toImageDto(imageList.get(0)) : null;
    }

}

