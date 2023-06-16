package com.kg.platform.online_course.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class OrderItem {
    private static final String SEQ_NAME = "order_item_seq";

    @Id
    @GeneratedValue(generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 4)
    private Long orderItemId;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Order order;
    private double totalPrice;
}
