package com.bhawanisingh.airesume.auth.entity;

import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                )
        }
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name",
            nullable = false,
            length = 100)
    private String fullName;

    @Column(nullable = false,
            length = 255)
    private String email;

    @Column(nullable = false,
            length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            length = 30)
    private UserStatus status;

    @Column(name = "email_verified",
            nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "profile_image_url",
            length = 500)
    private String profileImageUrl;

}