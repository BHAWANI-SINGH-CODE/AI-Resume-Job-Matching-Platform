package com.bhawanisingh.airesume.resumeparse.repository;

import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParsedResumeExperienceRepository extends JpaRepository<ParsedResumeExperience, Long> {
}