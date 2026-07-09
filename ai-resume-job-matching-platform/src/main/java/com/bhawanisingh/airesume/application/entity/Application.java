package com.bhawanisingh.airesume.application.entity;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_candidate_job",
                        columnNames = {"candidate_id", "job_id"}
                )
        },
        indexes = {
                @Index(name = "idx_application_candidate_id", columnList = "candidate_id"),
                @Index(name = "idx_application_job_id", columnList = "job_id"),
                @Index(name = "idx_application_status", columnList = "status"),
                @Index(name = "idx_application_resume_id", columnList = "resume_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cover_letter", length = 3000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApplicationStatus status;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @PrePersist
    public void prePersist() {
        if (this.appliedAt == null) {
            this.appliedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ApplicationStatus.APPLIED;
        }
    }
}