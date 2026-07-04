package com.bhawanisingh.airesume.resume.repository;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    List<Resume> findByUserAndStatusOrderByCreatedAtDesc(User user, ResumeStatus status);

    Optional<Resume> findByIdAndUser(UUID id, User user);

    Optional<Resume> findByIdAndUserEmailAndStatus(UUID id, String email, ResumeStatus status);

    Optional<Resume> findByUserAndPrimaryResumeTrueAndStatus(User user, ResumeStatus status);

    long countByUserAndStatus(User user, ResumeStatus status);
}