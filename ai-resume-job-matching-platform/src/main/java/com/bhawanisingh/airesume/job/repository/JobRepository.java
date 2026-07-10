package com.bhawanisingh.airesume.job.repository;

import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByIdAndDeletedFalse(Long id);

    List<Job> findAllByDeletedFalseOrderByCreatedAtDesc();

    List<Job> findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(JobStatus status);

    List<Job> findAllByCompanyNameContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String companyName);

    List<Job> findAllByLocationContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String location);

    List<Job> findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String title);

    @Query("""
            SELECT j
            FROM Job j
            WHERE (:search IS NULL
                    OR LOWER(j.title) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.location) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:status IS NULL OR j.status = :status)
              AND (:deleted IS NULL OR j.deleted = :deleted)
            """)
    Page<Job> searchAdminJobs(
            String search,
            JobStatus status,
            Boolean deleted,
            Pageable pageable
    );
}