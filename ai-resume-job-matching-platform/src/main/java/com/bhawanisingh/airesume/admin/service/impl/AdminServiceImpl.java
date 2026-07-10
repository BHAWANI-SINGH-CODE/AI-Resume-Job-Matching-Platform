package com.bhawanisingh.airesume.admin.service.impl;

import com.bhawanisingh.airesume.admin.dto.request.AdminUserStatusUpdateRequest;
import com.bhawanisingh.airesume.admin.dto.response.AdminApplicationPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminCompanyPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminDashboardSummaryResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminJobPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.admin.mapper.AdminMapper;
import com.bhawanisingh.airesume.admin.service.AdminService;
import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import com.bhawanisingh.airesume.application.repository.ApplicationRepository;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.company.entity.Company;
import com.bhawanisingh.airesume.company.repository.CompanyRepository;
import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import com.bhawanisingh.airesume.job.repository.JobRepository;
import com.bhawanisingh.airesume.resume.repository.ResumeRepository;
import com.bhawanisingh.airesume.resumeparse.repository.ParsedResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final ParsedResumeRepository parsedResumeRepository;

    @Override
    public AdminDashboardSummaryResponse getDashboardSummary() {
        long totalUsers = userRepository.count();
        long totalAdminAccounts = userRepository.countByRole(Role.ADMIN);
        long totalCompanyAccounts = userRepository.countByRole(Role.COMPANY);
        long totalCandidateAccounts = userRepository.countByRole(Role.USER);

        long totalCompanies = companyRepository.count();
        long totalJobs = jobRepository.count();
        long totalApplications = applicationRepository.count();
        long totalResumes = resumeRepository.count();
        long totalParsedResumes = parsedResumeRepository.count();

        return AdminDashboardSummaryResponse.builder()
                .totalUsers(totalUsers)
                .totalAdminAccounts(totalAdminAccounts)
                .totalCompanyAccounts(totalCompanyAccounts)
                .totalCandidateAccounts(totalCandidateAccounts)
                .totalCompanies(totalCompanies)
                .totalJobs(totalJobs)
                .totalApplications(totalApplications)
                .totalResumes(totalResumes)
                .totalParsedResumes(totalParsedResumes)
                .build();
    }

    @Override
    public AdminUserPageResponse getUsers(
            String search,
            Role role,
            UserStatus status,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        String normalizedSearch = normalize(search);

        Page<User> userPage = userRepository.searchAdminUsers(
                normalizedSearch,
                role,
                status,
                pageable
        );

        return AdminUserPageResponse.builder()
                .content(userPage.getContent().stream()
                        .map(AdminMapper::toAdminUserResponse)
                        .toList())
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public AdminUserResponse updateUserStatus(Long userId, AdminUserStatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setStatus(request.getStatus());
        User savedUser = userRepository.save(user);

        return AdminMapper.toAdminUserResponse(savedUser);
    }

    @Override
    public AdminCompanyPageResponse getCompanies(
            String search,
            Boolean deleted,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Company> companyPage = companyRepository.searchAdminCompanies(
                normalize(search),
                deleted,
                pageable
        );

        return AdminCompanyPageResponse.builder()
                .content(companyPage.getContent().stream()
                        .map(AdminMapper::toAdminCompanyResponse)
                        .toList())
                .page(companyPage.getNumber())
                .size(companyPage.getSize())
                .totalElements(companyPage.getTotalElements())
                .totalPages(companyPage.getTotalPages())
                .first(companyPage.isFirst())
                .last(companyPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void softDeleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        company.setDeleted(true);
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void restoreCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        company.setDeleted(false);
        companyRepository.save(company);
    }

    @Override
    public AdminJobPageResponse getJobs(
            String search,
            JobStatus status,
            Boolean deleted,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Job> jobPage = jobRepository.searchAdminJobs(
                normalize(search),
                status,
                deleted,
                pageable
        );

        return AdminJobPageResponse.builder()
                .content(jobPage.getContent().stream()
                        .map(AdminMapper::toAdminJobResponse)
                        .toList())
                .page(jobPage.getNumber())
                .size(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .first(jobPage.isFirst())
                .last(jobPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void softDeleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        job.setDeleted(true);
        jobRepository.save(job);
    }

    @Override
    @Transactional
    public void restoreJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        job.setDeleted(false);
        jobRepository.save(job);
    }

    @Override
    public AdminApplicationPageResponse getApplications(
            String search,
            ApplicationStatus status,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "appliedAt")
        );

        Page<Application> applicationPage = applicationRepository.searchAdminApplications(
                normalize(search),
                status,
                pageable
        );

        return AdminApplicationPageResponse.builder()
                .content(applicationPage.getContent().stream()
                        .map(AdminMapper::toAdminApplicationResponse)
                        .toList())
                .page(applicationPage.getNumber())
                .size(applicationPage.getSize())
                .totalElements(applicationPage.getTotalElements())
                .totalPages(applicationPage.getTotalPages())
                .first(applicationPage.isFirst())
                .last(applicationPage.isLast())
                .build();
    }

    private String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}