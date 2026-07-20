package com.bhawanisingh.airesume.auth.repository;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * ============================================================
     * Authentication
     * ============================================================
     */

    List<User> findAllByOrderByCreatedAtDesc();
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /*
     * ============================================================
     * Profile
     * ============================================================
     */

    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    /*
     * ============================================================
     * Dashboard Counts
     * ============================================================
     */

    long countByRole(Role role);

    long countByStatus(UserStatus status);

    /*
     * ============================================================
     * Admin Search
     * ============================================================
     */

    @Query("""
            SELECT u
            FROM User u
            WHERE
                (
                    :keyword IS NULL
                    OR :keyword = ''
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            AND
                (
                    :role IS NULL
                    OR u.role = :role
                )
            AND
                (
                    :status IS NULL
                    OR u.status = :status
                )
            ORDER BY u.createdAt DESC
            """)
    Page<User> searchAdminUsers(
            @Param("keyword") String keyword,
            @Param("role") Role role,
            @Param("status") UserStatus status,
            Pageable pageable
    );

}