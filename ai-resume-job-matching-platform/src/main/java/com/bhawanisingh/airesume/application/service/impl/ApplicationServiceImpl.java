package com.bhawanisingh.airesume.application.service.impl;

import com.bhawanisingh.airesume.application.dto.request.ApplicationStatusUpdateRequest;
import com.bhawanisingh.airesume.application.dto.request.JobApplicationRequest;
import com.bhawanisingh.airesume.application.dto.response.ApplicationResponse;
import com.bhawanisingh.airesume.application.dto.response.JobApplicationSummaryResponse;
import com.bhawanisingh.airesume.application.dto.response.RecruiterApplicationDashboardResponse;
import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import com.bhawanisingh.airesume.application.mapper.ApplicationMapper;
import com.bhawanisingh.airesume.application.repository.ApplicationRepository;
import com.bhawanisingh.airesume.application.service.ApplicationService;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.AccessDeniedException;
import com.bhawanisingh.airesume.common.exception.InvalidOperationException;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import com.bhawanisingh.airesume.job.repository.JobRepository;
import com.bhawanisingh.airesume.notification.enums.NotificationType;
import com.bhawanisingh.airesume.notification.service.NotificationService;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import com.bhawanisingh.airesume.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplicationMapper applicationMapper;
    private final NotificationService notificationService;

    @Override
    public ApplicationResponse applyToJob(Long jobId, JobApplicationRequest request) {
        User candidate = getCurrentUser();
        validateCandidateCanApply(candidate);

        Job job = jobRepository.findByIdAndDeletedFalse(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        validateJobIsOpenForApplications(job);

        if (applicationRepository.existsByCandidate_IdAndJob_Id(candidate.getId(), jobId)) {
            throw new InvalidOperationException("You have already applied to this job");
        }

        Resume selectedResume = resolveResumeForApplication(candidate, request.getResumeId());

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setResume(selectedResume);
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.APPLIED);

        Application savedApplication = applicationRepository.save(application);

        createApplicationSubmittedNotification(candidate, job, savedApplication);

        return applicationMapper.toApplicationResponse(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {
        User currentUser = getCurrentUser();

        return applicationRepository.findByCandidate_IdOrderByAppliedAtDesc(currentUser.getId())
                .stream()
                .map(applicationMapper::toApplicationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplicationById(Long applicationId) {
        User currentUser = getCurrentUser();

        Application application = applicationRepository.findByIdAndCandidate_Id(applicationId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                ));

        return applicationMapper.toApplicationResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationSummaryResponse> getApplicationsByJobId(Long jobId) {
        User currentUser = getCurrentUser();

        Job job = jobRepository.findByIdAndDeletedFalse(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        validateJobOwnership(job, currentUser);

        return applicationRepository.findByJob_IdOrderByAppliedAtDesc(job.getId())
                .stream()
                .map(applicationMapper::toJobApplicationSummaryResponse)
                .toList();
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request) {
        User currentUser = getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                ));

        validateJobOwnership(application.getJob(), currentUser);
        validateStatusTransition(application.getStatus(), request.getStatus());

        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(request.getStatus());

        Application updatedApplication = applicationRepository.save(application);

        createApplicationStatusUpdatedNotification(updatedApplication, oldStatus, request.getStatus());

        return applicationMapper.toApplicationResponse(updatedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationSummaryResponse> getMyPostedJobApplications() {
        User currentUser = getCurrentUser();
        validateCompanyOrAdminAccess(currentUser);

        if (currentUser.getRole() == Role.ADMIN) {
            return applicationRepository.findAll()
                    .stream()
                    .map(applicationMapper::toJobApplicationSummaryResponse)
                    .toList();
        }

        return applicationRepository.findByJob_PostedByUserIdOrderByAppliedAtDesc(currentUser.getId())
                .stream()
                .map(applicationMapper::toJobApplicationSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationSummaryResponse> getMyPostedJobApplicationsByStatus(ApplicationStatus status) {
        User currentUser = getCurrentUser();
        validateCompanyOrAdminAccess(currentUser);

        if (currentUser.getRole() == Role.ADMIN) {
            return applicationRepository.findAll()
                    .stream()
                    .filter(application -> application.getStatus() == status)
                    .map(applicationMapper::toJobApplicationSummaryResponse)
                    .toList();
        }

        return applicationRepository.findByJob_PostedByUserIdAndStatusOrderByAppliedAtDesc(currentUser.getId(), status)
                .stream()
                .map(applicationMapper::toJobApplicationSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterApplicationDashboardResponse getMyApplicationDashboard() {
        User currentUser = getCurrentUser();
        validateCompanyOrAdminAccess(currentUser);

        if (currentUser.getRole() == Role.ADMIN) {
            List<Application> allApplications = applicationRepository.findAll();

            return RecruiterApplicationDashboardResponse.builder()
                    .totalApplications(allApplications.size())
                    .appliedCount(countByStatus(allApplications, ApplicationStatus.APPLIED))
                    .reviewedCount(countByStatus(allApplications, ApplicationStatus.REVIEWED))
                    .shortlistedCount(countByStatus(allApplications, ApplicationStatus.SHORTLISTED))
                    .rejectedCount(countByStatus(allApplications, ApplicationStatus.REJECTED))
                    .hiredCount(countByStatus(allApplications, ApplicationStatus.HIRED))
                    .build();
        }

        Long recruiterId = currentUser.getId();

        return RecruiterApplicationDashboardResponse.builder()
                .totalApplications(applicationRepository.countByJob_PostedByUserId(recruiterId))
                .appliedCount(applicationRepository.countByJob_PostedByUserIdAndStatus(recruiterId, ApplicationStatus.APPLIED))
                .reviewedCount(applicationRepository.countByJob_PostedByUserIdAndStatus(recruiterId, ApplicationStatus.REVIEWED))
                .shortlistedCount(applicationRepository.countByJob_PostedByUserIdAndStatus(recruiterId, ApplicationStatus.SHORTLISTED))
                .rejectedCount(applicationRepository.countByJob_PostedByUserIdAndStatus(recruiterId, ApplicationStatus.REJECTED))
                .hiredCount(applicationRepository.countByJob_PostedByUserIdAndStatus(recruiterId, ApplicationStatus.HIRED))
                .build();
    }

    private void createApplicationSubmittedNotification(User candidate, Job job, Application application) {
        String jobTitle = job.getTitle() != null ? job.getTitle() : "job";
        String companyName = job.getCompanyName() != null ? job.getCompanyName() : "the company";

        notificationService.createNotification(
                candidate.getId(),
                "Application submitted",
                "Your application for " + jobTitle + " at " + companyName + " has been submitted successfully.",
                NotificationType.APPLICATION_SUBMITTED,
                application.getId(),
                "APPLICATION"
        );
    }

    private void createApplicationStatusUpdatedNotification(
            Application application,
            ApplicationStatus oldStatus,
            ApplicationStatus newStatus
    ) {
        User candidate = application.getCandidate();
        Job job = application.getJob();

        if (candidate == null || candidate.getId() == null || job == null) {
            return;
        }

        String jobTitle = job.getTitle() != null ? job.getTitle() : "job";
        String companyName = job.getCompanyName() != null ? job.getCompanyName() : "the company";

        String title;
        String message;
        NotificationType notificationType;

        switch (newStatus) {
            case REVIEWED -> {
                title = "Application reviewed";
                message = "Your application for " + jobTitle + " at " + companyName + " has been reviewed.";
                notificationType = NotificationType.APPLICATION_REVIEWED;
            }
            case SHORTLISTED -> {
                title = "Application shortlisted";
                message = "Congratulations! You have been shortlisted for " + jobTitle + " at " + companyName + ".";
                notificationType = NotificationType.APPLICATION_SHORTLISTED;
            }
            case REJECTED -> {
                title = "Application update";
                message = "Your application for " + jobTitle + " at " + companyName + " has been rejected.";
                notificationType = NotificationType.APPLICATION_REJECTED;
            }
            case HIRED -> {
                title = "Application hired";
                message = "Congratulations! You have been selected for " + jobTitle + " at " + companyName + ".";
                notificationType = NotificationType.APPLICATION_HIRED;
            }
            default -> {
                return;
            }
        }

        notificationService.createNotification(
                candidate.getId(),
                title,
                message,
                notificationType,
                application.getId(),
                "APPLICATION"
        );
    }

    private void validateCandidateCanApply(User user) {
        if (user.getRole() != Role.USER) {
            throw new AccessDeniedException("Only candidate users can apply to jobs");
        }
    }

    private void validateCompanyOrAdminAccess(User currentUser) {
        if (currentUser.getRole() != Role.COMPANY && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only company or admin users can access recruiter application data");
        }
    }

    private void validateJobOwnership(Job job, User currentUser) {
        validateCompanyOrAdminAccess(currentUser);

        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        if (job.getPostedByUserId() == null || !job.getPostedByUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to manage applications for this job");
        }
    }

    private void validateJobIsOpenForApplications(Job job) {
        if (job.getStatus() != JobStatus.OPEN) {
            throw new InvalidOperationException("Applications are allowed only for OPEN jobs");
        }

        LocalDate deadline = job.getApplicationDeadline();
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new InvalidOperationException("Application deadline has already passed for this job");
        }
    }

    private Resume resolveResumeForApplication(User candidate, Long resumeId) {
        if (resumeId != null) {
            return resumeRepository.findByIdAndUserAndStatus(resumeId, candidate, ResumeStatus.ACTIVE)
                    .orElseThrow(() -> new InvalidOperationException(
                            "Selected resume does not belong to the current user or is not active"
                    ));
        }

        return resumeRepository.findByUserAndPrimaryResumeTrueAndStatus(candidate, ResumeStatus.ACTIVE)
                .orElse(null);
    }

    private void validateStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        if (currentStatus == newStatus) {
            throw new InvalidOperationException("Application is already in status: " + currentStatus);
        }

        Set<ApplicationStatus> allowedNextStatuses = switch (currentStatus) {
            case APPLIED -> EnumSet.of(ApplicationStatus.REVIEWED, ApplicationStatus.REJECTED);
            case REVIEWED -> EnumSet.of(ApplicationStatus.SHORTLISTED, ApplicationStatus.REJECTED);
            case SHORTLISTED -> EnumSet.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED);
            case REJECTED, HIRED -> EnumSet.noneOf(ApplicationStatus.class);
        };

        if (!allowedNextStatuses.contains(newStatus)) {
            throw new InvalidOperationException(
                    "Invalid application status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    private long countByStatus(List<Application> applications, ApplicationStatus status) {
        return applications.stream()
                .filter(application -> application.getStatus() == status)
                .count();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}