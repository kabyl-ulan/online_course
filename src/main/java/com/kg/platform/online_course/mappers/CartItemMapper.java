package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.CartItemResponse;
import com.kg.platform.online_course.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = { CourseMapper.class})
public interface CartItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.evalTotalPrice())")
    @Mapping(target = "cartItemId", source = "id")
    CartItemResponse toCartItemResponse(CartItem cartItem);


}
