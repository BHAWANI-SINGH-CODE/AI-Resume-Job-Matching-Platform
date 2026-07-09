package com.bhawanisingh.airesume.resume.repository;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserAndStatusOrderByCreatedAtDesc(User user, ResumeStatus status);

    Optional<Resume> findByIdAndUser(Long id, User user);

    Optional<Resume> findByIdAndUserAndStatus(Long id, User user, ResumeStatus status);

    Optional<Resume> findByIdAndUserEmailAndStatus(Long id, String email, ResumeStatus status);

    Optional<Resume> findByUserAndPrimaryResumeTrueAndStatus(User user, ResumeStatus status);

    long countByUserAndStatus(User user, ResumeStatus status);
}