package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
//    @EntityGraph(value = "product-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    Page<Course> findAllByCategoryIdOrderByUploadDateDesc(Long categoryId, Pageable pageable);

//    @EntityGraph(value = "product-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    Page<Course> findByCourseNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

//    @EntityGraph(value = "product-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    Page<Course> findByCourseNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryId(String name, String description, Long categoryId, Pageable pageable);

//    @EntityGraph(value = "product-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT p FROM Course p ORDER BY p.uploadDate DESC LIMIT 20")
    List<Course> findLatestProducts();
//    @EntityGraph(value = "product-entity-graph",type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT p FROM Course p ORDER BY p.sold DESC LIMIT 20")
    List<Course> findTopBySoldOrderBySoldDesc();

    /* This is jpql query
    @Query("SELECT p FROM Product p "
            + "JOIN FETCH p.category "
            + "LEFT JOIN FETCH p.imageList "
            + "LEFT JOIN FETCH p.reviews r "
            + "LEFT JOIN FETCH r.user "
            + "WHERE p.id = :id")
     **/
//    @EntityGraph(value = "product-entity-graph-with-reviews",type = EntityGraph.EntityGraphType.FETCH)
    Optional<Course> findById(Long id);

}
