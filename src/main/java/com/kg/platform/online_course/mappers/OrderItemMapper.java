package com.kg.platform.online_course.mappers;

import com.kg.platform.online_course.dto.response.OrderItemResponse;
import com.kg.platform.online_course.models.CartItem;
import com.kg.platform.online_course.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {CartItemMapper.class, ProductMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.evalTotalPrice())")
    @Mapping(target = "orderItemId", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(target = "productResponse", source = "product")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
