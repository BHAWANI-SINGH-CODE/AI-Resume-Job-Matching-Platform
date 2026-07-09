package com.bhawanisingh.airesume.application.repository;

import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByCandidate_IdAndJob_Id(Long candidateId, Long jobId);

    List<Application> findByCandidate_IdOrderByAppliedAtDesc(Long candidateId);

    List<Application> findByJob_IdOrderByAppliedAtDesc(Long jobId);

    Optional<Application> findByIdAndCandidate_Id(Long applicationId, Long candidateId);

    List<Application> findByJob_PostedByUserIdOrderByAppliedAtDesc(Long postedByUserId);

    List<Application> findByJob_PostedByUserIdAndStatusOrderByAppliedAtDesc(Long postedByUserId, ApplicationStatus status);

    long countByJob_PostedByUserId(Long postedByUserId);

    long countByJob_PostedByUserIdAndStatus(Long postedByUserId, ApplicationStatus status);
}