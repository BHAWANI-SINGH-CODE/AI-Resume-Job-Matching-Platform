package com.bhawanisingh.airesume.resumeparse.repository;

import com.bhawanisingh.airesume.resumeparse.entity.ParsedResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParsedResumeRepository extends JpaRepository<ParsedResume, Long> {

    Optional<ParsedResume> findByResumeId(Long resumeId);

    boolean existsByResumeId(Long resumeId);
}