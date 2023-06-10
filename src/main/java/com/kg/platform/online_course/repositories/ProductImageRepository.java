package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<Image,Long> {
}
