package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByUserEmail(String email);
}