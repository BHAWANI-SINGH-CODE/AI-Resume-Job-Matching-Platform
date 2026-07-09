package com.bhawanisingh.airesume.resumeparse.parser.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResumeParseResult {

    private String rawText;
    private String professionalSummary;
    private String currentTitle;
    private String currentCompany;
    private String phone;
    private String location;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private BigDecimal totalExperienceYears;

    private List<ParsedSkillData> skills = new ArrayList<>();
    private List<ParsedEducationData> educations = new ArrayList<>();
    private List<ParsedExperienceData> experiences = new ArrayList<>();

    public ResumeParseResult() {
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

    public BigDecimal getTotalExperienceYears() {
        return totalExperienceYears;
    }

    public void setTotalExperienceYears(BigDecimal totalExperienceYears) {
        this.totalExperienceYears = totalExperienceYears;
    }

    public List<ParsedSkillData> getSkills() {
        return skills;
    }

    public void setSkills(List<ParsedSkillData> skills) {
        this.skills = skills;
    }

    public List<ParsedEducationData> getEducations() {
        return educations;
    }

    public void setEducations(List<ParsedEducationData> educations) {
        this.educations = educations;
    }

    public List<ParsedExperienceData> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ParsedExperienceData> experiences) {
        this.experiences = experiences;
    }
}