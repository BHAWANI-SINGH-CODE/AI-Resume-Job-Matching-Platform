package com.bhawanisingh.airesume.company.repository;

import com.bhawanisingh.airesume.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByDeletedFalseOrderByCreatedAtDesc();

    Optional<Company> findByIdAndDeletedFalse(Long id);

    @Query("""
            SELECT c
            FROM Company c
            WHERE (:search IS NULL
                    OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.location) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:deleted IS NULL OR c.deleted = :deleted)
            """)
    Page<Company> searchAdminCompanies(
            String search,
            Boolean deleted,
            Pageable pageable
    );
}