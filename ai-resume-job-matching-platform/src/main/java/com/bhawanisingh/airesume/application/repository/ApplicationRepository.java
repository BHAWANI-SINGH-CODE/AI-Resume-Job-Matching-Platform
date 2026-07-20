package com.bhawanisingh.airesume.application.repository;

import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /*
     * ============================================================
     * Candidate APIs
     * ============================================================
     */

    boolean existsByCandidate_IdAndJob_Id(Long candidateId, Long jobId);

    List<Application> findByCandidate_IdOrderByAppliedAtDesc(Long candidateId);

    Optional<Application> findByIdAndCandidate_Id(Long applicationId, Long candidateId);

    /*
     * ============================================================
     * Recruiter APIs
     * ============================================================
     */

    List<Application> findByJob_IdOrderByAppliedAtDesc(Long jobId);

    List<Application> findByJob_PostedByUserIdOrderByAppliedAtDesc(Long recruiterId);

    List<Application> findByJob_PostedByUserIdAndStatusOrderByAppliedAtDesc(
            Long recruiterId,
            ApplicationStatus status
    );

    /*
     * ============================================================
     * Dashboard Counts
     * ============================================================
     */

    long countByJob_PostedByUserId(Long recruiterId);

    long countByJob_PostedByUserIdAndStatus(
            Long recruiterId,
            ApplicationStatus status
    );

    /*
     * ============================================================
     * Admin Search
     * ============================================================
     */

    @Query("""
            SELECT a
            FROM Application a
            WHERE
            (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(a.candidate.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.candidate.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.job.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.job.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND
            (
                :status IS NULL
                OR a.status = :status
            )
            ORDER BY a.appliedAt DESC
            """)
    Page<Application> searchAdminApplications(
            @Param("keyword") String keyword,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

}