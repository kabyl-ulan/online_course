package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Order;
import com.kg.platform.online_course.models.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Modifying
    @Query("DELETE FROM OrderItem o where o.product.id = :productId")
    void deleteByProductId(Long productId);


    @EntityGraph(value = "order-item-entity-graph")
    List<OrderItem> findByOrder(Order order);
}
