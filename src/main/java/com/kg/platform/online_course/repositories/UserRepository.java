package com.kg.platform.online_course.repositories;

import com.kg.platform.online_course.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("select u from User u where u.role = 'ADMIN' or u.role = 'USER'")
    List<User> findUsers();
}
