package com.bhawanisingh.airesume.auth.repository;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    @Query("""
            SELECT u
            FROM User u
            WHERE (:search IS NULL
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:role IS NULL OR u.role = :role)
              AND (:status IS NULL OR u.status = :status)
            """)
    Page<User> searchAdminUsers(
            String search,
            Role role,
            UserStatus status,
            Pageable pageable
    );
}