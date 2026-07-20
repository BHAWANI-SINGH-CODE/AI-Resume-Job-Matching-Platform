package com.bhawanisingh.airesume.resumeparse.entity;

import com.bhawanisingh.airesume.common.entity.BaseEntity;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.enums.ParsingStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "parsed_resumes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_parsed_resume_resume_id", columnNames = "resume_id")
        }
)
public class ParsedResume extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "resume_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_parsed_resume_resume")
    )
    private Resume resume;

    @Enumerated(EnumType.STRING)
    @Column(name = "parsing_status", nullable = false, length = 30)
    private ParsingStatus parsingStatus = ParsingStatus.NOT_PARSED;

    @Lob
    @Column(name = "raw_text", columnDefinition = "TEXT")
    private String rawText;

    @Column(name = "professional_summary", columnDefinition = "TEXT")
    private String professionalSummary;

    @Column(name = "total_experience_years", precision = 5, scale = 2)
    private BigDecimal totalExperienceYears;

    @Column(name = "current_title", length = 150)
    private String currentTitle;

    @Column(name = "current_company", length = 150)
    private String currentCompany;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "location", length = 150)
    private String location;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @Column(name = "parsed_at")
    private LocalDateTime parsedAt;

    @Column(name = "parse_error", columnDefinition = "TEXT")
    private String parseError;

    @OneToMany(
            mappedBy = "parsedResume",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ParsedResumeSkill> skills = new ArrayList<>();

    @OneToMany(
            mappedBy = "parsedResume",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ParsedResumeEducation> educations = new ArrayList<>();

    @OneToMany(
            mappedBy = "parsedResume",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ParsedResumeExperience> experiences = new ArrayList<>();

    public ParsedResume() {
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public ParsingStatus getParsingStatus() {
        return parsingStatus;
    }

    public void setParsingStatus(ParsingStatus parsingStatus) {
        this.parsingStatus = parsingStatus;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public BigDecimal getTotalExperienceYears() {
        return totalExperienceYears;
    }

    public void setTotalExperienceYears(BigDecimal totalExperienceYears) {
        this.totalExperienceYears = totalExperienceYears;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public LocalDateTime getParsedAt() {
        return parsedAt;
    }

    public void setParsedAt(LocalDateTime parsedAt) {
        this.parsedAt = parsedAt;
    }

    public String getParseError() {
        return parseError;
    }

    public void setParseError(String parseError) {
        this.parseError = parseError;
    }

    public List<ParsedResumeSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<ParsedResumeSkill> skills) {
        this.skills = skills;
    }

    public List<ParsedResumeEducation> getEducations() {
        return educations;
    }

    public void setEducations(List<ParsedResumeEducation> educations) {
        this.educations = educations;
    }

    public List<ParsedResumeExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ParsedResumeExperience> experiences) {
        this.experiences = experiences;
    }

    public void addSkill(ParsedResumeSkill skill) {
        skill.setParsedResume(this);
        this.skills.add(skill);
    }

    public void addEducation(ParsedResumeEducation education) {
        education.setParsedResume(this);
        this.educations.add(education);
    }

    public void addExperience(ParsedResumeExperience experience) {
        experience.setParsedResume(this);
        this.experiences.add(experience);
    }

    public void clearSkills() {
        this.skills.clear();
    }

    public void clearEducations() {
        this.educations.clear();
    }

    public void clearExperiences() {
        this.experiences.clear();
    }
}