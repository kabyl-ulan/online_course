package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Course;
import com.kg.platform.online_course.models.Review;
import com.kg.platform.online_course.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByCourseOrderByDateDesc(Course course);
    List<Review> findByUser(User user);

    List<Review> findAllByCourseId(Long courseId);
}
