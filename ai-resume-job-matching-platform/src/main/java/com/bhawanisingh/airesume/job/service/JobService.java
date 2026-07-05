package com.bhawanisingh.airesume.job.service;

import com.bhawanisingh.airesume.job.dto.JobCreateRequest;
import com.bhawanisingh.airesume.job.dto.JobResponse;
import com.bhawanisingh.airesume.job.dto.JobStatusUpdateRequest;
import com.bhawanisingh.airesume.job.dto.JobUpdateRequest;

import java.util.List;

public interface JobService {

    JobResponse createJob(JobCreateRequest request, Long postedByUserId);

    List<JobResponse> getAllOpenJobs();

    List<JobResponse> getAllJobs();

    JobResponse getJobById(Long jobId);

    JobResponse updateJob(Long jobId, JobUpdateRequest request);

    JobResponse updateJobStatus(Long jobId, JobStatusUpdateRequest request);

    void deleteJob(Long jobId);
}