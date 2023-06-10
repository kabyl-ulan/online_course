package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Cart;
import com.kg.platform.online_course.models.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    @EntityGraph(value = "cart-item-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    CartItem findByProductIdAndAndCart_Id(Long productId, Long cartId);

    @EntityGraph(value = "cart-item-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    List<CartItem> findByCartOrderById(Cart cart);

    @Modifying
    @Query("DELETE FROM CartItem c where c.product.id = :productId")
    void deleteByProductId(Long productId);

    @EntityGraph(value = "cart-item-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    Optional<CartItem> findById(Long id);

}