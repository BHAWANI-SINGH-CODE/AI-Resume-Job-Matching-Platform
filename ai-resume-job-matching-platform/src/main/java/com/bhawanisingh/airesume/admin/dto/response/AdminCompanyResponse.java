package com.bhawanisingh.airesume.admin.dto.response;

import com.bhawanisingh.airesume.company.enums.CompanySize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCompanyResponse {

    private Long id;
    private String name;
    private String website;
    private String industry;
    private String location;
    private String description;
    private String logoUrl;
    private CompanySize size;
    private Integer foundedYear;
    private Long createdByUserId;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}