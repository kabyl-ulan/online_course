package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.CartItemResponse;
import com.kg.platform.online_course.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = { ProductMapper.class})
public interface CartItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.evalTotalPrice())")
    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productResponse", source = "product")
    CartItemResponse toCartItemResponse(CartItem cartItem);


}
