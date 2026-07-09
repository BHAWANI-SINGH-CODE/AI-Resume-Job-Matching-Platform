package com.bhawanisingh.airesume.resumeparse.dto.response;

import com.bhawanisingh.airesume.resumeparse.enums.ParsingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResumeResponse {

    private Long parsedResumeId;
    private Long resumeId;

    private ParsingStatus parsingStatus;
    private String rawText;
    private String professionalSummary;
    private BigDecimal totalExperienceYears;
    private String currentTitle;
    private String currentCompany;
    private String phone;
    private String location;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private LocalDateTime parsedAt;
    private String parseError;

    private List<ParsedResumeSkillResponse> skills;
    private List<ParsedResumeEducationResponse> educations;
    private List<ParsedResumeExperienceResponse> experiences;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}