package com.kg.platform.online_course.dto.response;


import com.kg.platform.online_course.models.enums.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailsResponse {
    private Long id;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private double totalPrice;
    private List<OrderItemResponse> orderItems;
    private UserResponse customer;
}
