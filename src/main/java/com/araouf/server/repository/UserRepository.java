package com.araouf.server.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.araouf.server.domain.db.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR u.fname LIKE %:search%) OR " +
            "(:search IS NULL OR u.lname LIKE %:search%) OR " +
            "(:search IS NULL OR u.email LIKE %:search%) OR " +
            "(:search IS NULL OR u.phone LIKE %:search%)")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);

}
