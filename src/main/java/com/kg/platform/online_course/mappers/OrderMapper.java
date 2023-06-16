package com.kg.platform.online_course.mappers;


import com.kg.platform.online_course.dto.response.OrderDetailsResponse;
import com.kg.platform.online_course.dto.response.OrderResponse;
import com.kg.platform.online_course.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {OrderItemMapper.class, CartItemMapper.class, CourseMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(target = "customer",source = "user")
    OrderDetailsResponse toOrderDetailsResponse(Order order);

    @Mapping(target = "customer",source = "user")
    OrderResponse toOrderResponse(Order order);
}
