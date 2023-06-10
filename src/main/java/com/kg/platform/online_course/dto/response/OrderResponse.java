package com.kg.platform.online_course.dto.response;

import com.kg.platform.online_course.models.enums.OrderStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private double totalPrice;
    private UserResponse customer;
}
