package com.bhawanisingh.airesume.job.repository;

import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByIdAndDeletedFalse(Long id);

    List<Job> findAllByDeletedFalseOrderByCreatedAtDesc();

    List<Job> findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(JobStatus status);

    List<Job> findAllByCompanyNameContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String companyName);

    List<Job> findAllByLocationContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String location);

    List<Job> findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(String title);
}