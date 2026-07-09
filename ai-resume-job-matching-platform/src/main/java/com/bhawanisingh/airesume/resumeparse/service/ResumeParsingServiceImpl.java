package com.bhawanisingh.airesume.resumeparse.service;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import com.bhawanisingh.airesume.resume.repository.ResumeRepository;
import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeEducationResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeExperienceResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeSkillResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ResumeParsingTriggerResponse;
import com.bhawanisingh.airesume.resumeparse.entity.ParsedResume;
import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeEducation;
import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeExperience;
import com.bhawanisingh.airesume.resumeparse.entity.ParsedResumeSkill;
import com.bhawanisingh.airesume.resumeparse.enums.ParsingStatus;
import com.bhawanisingh.airesume.resumeparse.repository.ParsedResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeParsingServiceImpl implements ResumeParsingService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ParsedResumeRepository parsedResumeRepository;

    @Override
    public ResumeParsingTriggerResponse triggerResumeParsing(Long resumeId, String userEmail) {

        User user = getUserByEmail(userEmail);
        Resume resume = getActiveResumeByIdAndUser(resumeId, user);

        ParsedResume parsedResume = parsedResumeRepository.findByResumeId(resume.getId())
                .orElseGet(() -> createInitialParsedResume(resume));

        parsedResume.setParsingStatus(ParsingStatus.PROCESSING);
        parsedResume.setParseError(null);
        parsedResumeRepository.save(parsedResume);

        runMockParsing(parsedResume, resume);

        ParsedResume savedParsedResume = parsedResumeRepository.save(parsedResume);

        return ResumeParsingTriggerResponse.builder()
                .parsedResumeId(savedParsedResume.getId())
                .resumeId(resume.getId())
                .parsingStatus(savedParsedResume.getParsingStatus())
                .message("Resume parsing completed successfully")
                .parsedAt(savedParsedResume.getParsedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ParsedResumeResponse getParsedResumeByResumeId(Long resumeId, String userEmail) {

        User user = getUserByEmail(userEmail);
        Resume resume = getActiveResumeByIdAndUser(resumeId, user);

        ParsedResume parsedResume = parsedResumeRepository.findByResumeId(resume.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parsed resume not found for resume id: " + resumeId
                ));

        return mapToParsedResumeResponse(parsedResume);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));
    }

    private Resume getActiveResumeByIdAndUser(Long resumeId, User user) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found with id: " + resumeId));

        if (resume.getStatus() != ResumeStatus.ACTIVE) {
            throw new ResourceNotFoundException("Resume not found with id: " + resumeId);
        }

        return resume;
    }

    private ParsedResume createInitialParsedResume(Resume resume) {
        ParsedResume parsedResume = new ParsedResume();
        parsedResume.setResume(resume);
        parsedResume.setParsingStatus(ParsingStatus.NOT_PARSED);
        return parsedResume;
    }

    private void runMockParsing(ParsedResume parsedResume, Resume resume) {

        parsedResume.clearSkills();
        parsedResume.clearEducations();
        parsedResume.clearExperiences();

        parsedResume.setRawText(buildMockRawText(resume));
        parsedResume.setProfessionalSummary("Mock parsed summary for resume: " + resume.getOriginalFileName());
        parsedResume.setCurrentTitle("Not parsed yet");
        parsedResume.setCurrentCompany("Not parsed yet");
        parsedResume.setPhone(null);
        parsedResume.setLocation(null);
        parsedResume.setLinkedinUrl(null);
        parsedResume.setGithubUrl(null);
        parsedResume.setPortfolioUrl(null);
        parsedResume.setTotalExperienceYears(null);
        parsedResume.setParseError(null);
        parsedResume.setParsedAt(LocalDateTime.now());
        parsedResume.setParsingStatus(ParsingStatus.PARSED);

        addMockSkills(parsedResume);
    }

    private String buildMockRawText(Resume resume) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mock parsed content for resume file: ")
                .append(resume.getOriginalFileName());

        if (resume.getFileType() != null) {
            sb.append(" | fileType=").append(resume.getFileType());
        }

        if (resume.getFileSize() != null) {
            sb.append(" | fileSize=").append(resume.getFileSize());
        }

        return sb.toString();
    }

    private void addMockSkills(ParsedResume parsedResume) {
        List<String> defaultSkills = List.of("Java", "Spring Boot", "SQL");

        for (String skillName : defaultSkills) {
            ParsedResumeSkill skill = new ParsedResumeSkill();
            skill.setSkillName(skillName);
            skill.setNormalizedSkillName(skillName.toLowerCase());
            parsedResume.addSkill(skill);
        }
    }

    private ParsedResumeResponse mapToParsedResumeResponse(ParsedResume parsedResume) {
        return ParsedResumeResponse.builder()
                .parsedResumeId(parsedResume.getId())
                .resumeId(parsedResume.getResume().getId())
                .parsingStatus(parsedResume.getParsingStatus())
                .rawText(parsedResume.getRawText())
                .professionalSummary(parsedResume.getProfessionalSummary())
                .totalExperienceYears(parsedResume.getTotalExperienceYears())
                .currentTitle(parsedResume.getCurrentTitle())
                .currentCompany(parsedResume.getCurrentCompany())
                .phone(parsedResume.getPhone())
                .location(parsedResume.getLocation())
                .linkedinUrl(parsedResume.getLinkedinUrl())
                .githubUrl(parsedResume.getGithubUrl())
                .portfolioUrl(parsedResume.getPortfolioUrl())
                .parsedAt(parsedResume.getParsedAt())
                .parseError(parsedResume.getParseError())
                .skills(mapSkills(parsedResume.getSkills()))
                .educations(mapEducations(parsedResume.getEducations()))
                .experiences(mapExperiences(parsedResume.getExperiences()))
                .createdAt(parsedResume.getCreatedAt())
                .updatedAt(parsedResume.getUpdatedAt())
                .build();
    }

    private List<ParsedResumeSkillResponse> mapSkills(List<ParsedResumeSkill> skills) {
        if (skills == null || skills.isEmpty()) {
            return new ArrayList<>();
        }

        return skills.stream()
                .map(skill -> ParsedResumeSkillResponse.builder()
                        .id(skill.getId())
                        .skillName(skill.getSkillName())
                        .normalizedSkillName(skill.getNormalizedSkillName())
                        .build())
                .toList();
    }

    private List<ParsedResumeEducationResponse> mapEducations(List<ParsedResumeEducation> educations) {
        if (educations == null || educations.isEmpty()) {
            return new ArrayList<>();
        }

        return educations.stream()
                .map(education -> ParsedResumeEducationResponse.builder()
                        .id(education.getId())
                        .institutionName(education.getInstitutionName())
                        .degree(education.getDegree())
                        .fieldOfStudy(education.getFieldOfStudy())
                        .startYear(education.getStartYear())
                        .endYear(education.getEndYear())
                        .grade(education.getGrade())
                        .build())
                .toList();
    }

    private List<ParsedResumeExperienceResponse> mapExperiences(List<ParsedResumeExperience> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return new ArrayList<>();
        }

        return experiences.stream()
                .map(experience -> ParsedResumeExperienceResponse.builder()
                        .id(experience.getId())
                        .companyName(experience.getCompanyName())
                        .jobTitle(experience.getJobTitle())
                        .startDate(experience.getStartDate())
                        .endDate(experience.getEndDate())
                        .currentlyWorking(experience.getCurrentlyWorking())
                        .description(experience.getDescription())
                        .build())
                .toList();
    }
}