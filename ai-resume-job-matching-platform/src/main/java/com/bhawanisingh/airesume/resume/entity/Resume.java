package com.bhawanisingh.airesume.resume.entity;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.common.entity.BaseEntity;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "resumes",
        indexes = {
                @Index(name = "idx_resume_user_id", columnList = "user_id"),
                @Index(name = "idx_resume_status", columnList = "status")
        }
)
public class Resume extends BaseEntity {

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, length = 255)
    private String storedFileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private Boolean primaryResume = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ResumeStatus status = ResumeStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_resume_user")
    )
    private User user;
}