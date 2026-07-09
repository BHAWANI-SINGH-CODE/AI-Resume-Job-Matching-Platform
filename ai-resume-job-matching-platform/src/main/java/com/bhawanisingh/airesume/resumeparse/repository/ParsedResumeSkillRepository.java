package com.bhawanisingh.airesume.resumeparse.repository;

import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParsedResumeSkillRepository extends JpaRepository<ParsedResumeSkill, Long> {
}