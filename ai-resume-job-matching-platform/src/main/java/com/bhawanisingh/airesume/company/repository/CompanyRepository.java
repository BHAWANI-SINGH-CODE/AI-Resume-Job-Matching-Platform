package com.bhawanisingh.airesume.company.repository;

import com.bhawanisingh.airesume.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByDeletedFalseOrderByCreatedAtDesc();

    Optional<Company> findByIdAndDeletedFalse(Long id);
}