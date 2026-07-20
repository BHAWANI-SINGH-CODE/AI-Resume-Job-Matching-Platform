package com.bhawanisingh.airesume.resumeparse.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeParseResult {

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

    @Builder.Default
    private List<ParsedSkillData> skills = new ArrayList<>();

    @Builder.Default
    private List<ParsedEducationData> educations = new ArrayList<>();

    @Builder.Default
    private List<ParsedExperienceData> experiences = new ArrayList<>();

}