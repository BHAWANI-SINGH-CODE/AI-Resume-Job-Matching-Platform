package com.bhawanisingh.airesume.job.service;

import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.common.exception.InvalidJobDataException;
import com.bhawanisingh.airesume.job.dto.JobCreateRequest;
import com.bhawanisingh.airesume.job.dto.JobResponse;
import com.bhawanisingh.airesume.job.dto.JobStatusUpdateRequest;
import com.bhawanisingh.airesume.job.dto.JobUpdateRequest;
import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import com.bhawanisingh.airesume.job.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public JobResponse createJob(JobCreateRequest request, Long postedByUserId) {
        validateSalaryRange(request.getSalaryMin(), request.getSalaryMax());

        Job job = new Job();
        job.setTitle(request.getTitle().trim());
        job.setCompanyName(request.getCompanyName().trim());
        job.setLocation(request.getLocation().trim());
        job.setDescription(request.getDescription().trim());
        job.setRequirements(request.getRequirements());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setWorkMode(request.getWorkMode());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setExternalApplyUrl(request.getExternalApplyUrl());
        job.setPostedByUserId(postedByUserId);
        job.setStatus(JobStatus.OPEN);
        job.setDeleted(false);

        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllOpenJobs() {
        return jobRepository.findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(JobStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAllByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        Job job = getActiveJobOrThrow(jobId);
        return mapToResponse(job);
    }

    @Override
    public JobResponse updateJob(Long jobId, JobUpdateRequest request) {
        validateSalaryRange(request.getSalaryMin(), request.getSalaryMax());

        Job job = getActiveJobOrThrow(jobId);

        job.setTitle(request.getTitle().trim());
        job.setCompanyName(request.getCompanyName().trim());
        job.setLocation(request.getLocation().trim());
        job.setDescription(request.getDescription().trim());
        job.setRequirements(request.getRequirements());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setWorkMode(request.getWorkMode());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setExternalApplyUrl(request.getExternalApplyUrl());

        Job updatedJob = jobRepository.save(job);
        return mapToResponse(updatedJob);
    }

    @Override
    public JobResponse updateJobStatus(Long jobId, JobStatusUpdateRequest request) {
        Job job = getActiveJobOrThrow(jobId);
        job.setStatus(request.getStatus());

        Job updatedJob = jobRepository.save(job);
        return mapToResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long jobId) {
        Job job = getActiveJobOrThrow(jobId);
        job.setDeleted(true);
        jobRepository.save(job);
    }

    private Job getActiveJobOrThrow(Long jobId) {
        return jobRepository.findByIdAndDeletedFalse(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }

    private void validateSalaryRange(java.math.BigDecimal salaryMin, java.math.BigDecimal salaryMax) {
        if (salaryMin != null && salaryMax != null && salaryMin.compareTo(salaryMax) > 0) {
            throw new InvalidJobDataException("Minimum salary cannot be greater than maximum salary");
        }
    }

    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCompanyName(job.getCompanyName());
        response.setLocation(job.getLocation());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirements());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());
        response.setEmploymentType(job.getEmploymentType());
        response.setExperienceLevel(job.getExperienceLevel());
        response.setWorkMode(job.getWorkMode());
        response.setStatus(job.getStatus());
        response.setApplicationDeadline(job.getApplicationDeadline());
        response.setExternalApplyUrl(job.getExternalApplyUrl());
        response.setPostedByUserId(job.getPostedByUserId());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());
        return response;
    }
}