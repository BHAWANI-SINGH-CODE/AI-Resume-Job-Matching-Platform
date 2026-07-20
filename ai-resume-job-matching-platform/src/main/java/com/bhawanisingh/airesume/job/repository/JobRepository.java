package com.bhawanisingh.airesume.job.repository;

import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    /*
     * ============================================================
     * Existing Job Module
     * ============================================================
     */

    List<Job> findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(JobStatus status);

    List<Job> findAllByDeletedFalseOrderByCreatedAtDesc();

    Optional<Job> findByIdAndDeletedFalse(Long id);

    List<Job> findByPostedByUserIdAndDeletedFalseOrderByCreatedAtDesc(Long postedByUserId);

    List<Job> findByCompanyNameIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String companyName);

    long countByDeletedFalse();

    long countByStatus(JobStatus status);

    long countByPostedByUserId(Long postedByUserId);

    boolean existsByIdAndDeletedFalse(Long id);

    /*
     * ============================================================
     * Admin Search
     * ============================================================
     */

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                (:keyword IS NULL OR :keyword = '' OR
                    LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND
                (:status IS NULL OR j.status = :status)
            AND
                (:deleted IS NULL OR j.deleted = :deleted)
            ORDER BY j.createdAt DESC
            """)
    Page<Job> searchAdminJobs(
            @Param("keyword") String keyword,
            @Param("status") JobStatus status,
            @Param("deleted") Boolean deleted,
            Pageable pageable
    );

}