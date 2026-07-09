package com.bhawanisingh.airesume.resumeparse.repository;

import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParsedResumeEducationRepository extends JpaRepository<ParsedResumeEducation, Long> {
}