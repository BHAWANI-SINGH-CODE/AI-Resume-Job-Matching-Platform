package com.bhawanisingh.airesume.resumeparse.service;

import com.bhawanisingh.airesume.ai.ResumeRatingService;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.notification.enums.NotificationType;
import com.bhawanisingh.airesume.notification.service.NotificationService;
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
import com.bhawanisingh.airesume.resumeparse.parser.engine.ResumeParsingOrchestrator;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedEducationData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedSkillData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
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

    private final ResumeRatingService resumeRatingService;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ParsedResumeRepository parsedResumeRepository;
    private final ResumeParsingOrchestrator resumeParsingOrchestrator;
    private final NotificationService notificationService;

    @Override
    public ResumeParsingTriggerResponse triggerResumeParsing(Long resumeId, String userEmail) {

        User user = getUserByEmail(userEmail);
        Resume resume = getActiveResumeByIdAndUser(resumeId, user);

        ParsedResume parsedResume = parsedResumeRepository.findByResumeId(resume.getId())
                .orElseGet(() -> createInitialParsedResume(resume));

        parsedResume.setParsingStatus(ParsingStatus.PROCESSING);
        parsedResume.setParseError(null);
        parsedResumeRepository.save(parsedResume);

        try {
            ResumeParseResult parseResult = resumeParsingOrchestrator.parseResume(resume);
            applyParseResult(parsedResume, parseResult);

            int score = resumeRatingService.calculatePerfectionScore(parsedResume);

            ParsedResume savedParsedResume = parsedResumeRepository.save(parsedResume);
            createResumeParsingSuccessNotification(user, resume, savedParsedResume);

            return ResumeParsingTriggerResponse.builder()
                    .parsedResumeId(savedParsedResume.getId())
                    .resumeId(resume.getId())
                    .parsingStatus(savedParsedResume.getParsingStatus())
                    .message("Resume parsing completed successfully")
                    .parsedAt(savedParsedResume.getParsedAt())
                    .build();

        } catch (Exception ex) {
            parsedResume.clearSkills();
            parsedResume.clearEducations();
            parsedResume.clearExperiences();
            parsedResume.setParsingStatus(ParsingStatus.FAILED);
            parsedResume.setParseError(ex.getMessage());
            parsedResume.setParsedAt(LocalDateTime.now());

            ParsedResume failedParsedResume = parsedResumeRepository.save(parsedResume);

            createResumeParsingFailureNotification(user, resume, failedParsedResume, ex);

            return ResumeParsingTriggerResponse.builder()
                    .parsedResumeId(failedParsedResume.getId())
                    .resumeId(resume.getId())
                    .parsingStatus(failedParsedResume.getParsingStatus())
                    .message("Resume parsing failed")
                    .parsedAt(failedParsedResume.getParsedAt())
                    .build();
        }
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

    private void createResumeParsingSuccessNotification(User user, Resume resume, ParsedResume parsedResume) {
        String fileName = resume.getOriginalFileName() != null ? resume.getOriginalFileName() : "resume";

        notificationService.createNotification(
                user.getId(),
                "Resume parsed successfully",
                "Your resume " + fileName + " has been parsed successfully.",
                NotificationType.RESUME_PARSED,
                parsedResume.getId(),
                "PARSED_RESUME"
        );
    }

    private void createResumeParsingFailureNotification(
            User user,
            Resume resume,
            ParsedResume parsedResume,
            Exception exception
    ) {
        String fileName = resume.getOriginalFileName() != null ? resume.getOriginalFileName() : "resume";
        String errorMessage = exception.getMessage() != null ? exception.getMessage() : "Unknown parsing error";

        notificationService.createNotification(
                user.getId(),
                "Resume parsing failed",
                "Parsing failed for resume " + fileName + ". Reason: " + errorMessage,
                NotificationType.RESUME_PARSE_FAILED,
                parsedResume.getId(),
                "PARSED_RESUME"
        );
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

    private void applyParseResult(ParsedResume parsedResume, ResumeParseResult parseResult) {
        ParsedResume savedParsedResume = parsedResumeRepository.save(parsedResume);
        int score = resumeRatingService.calculatePerfectionScore(savedParsedResume);
        parsedResume.clearSkills();
        parsedResume.clearEducations();
        parsedResume.clearExperiences();

        parsedResume.setRawText(parseResult.getRawText());
        parsedResume.setProfessionalSummary(parseResult.getProfessionalSummary());
        parsedResume.setCurrentTitle(parseResult.getCurrentTitle());
        parsedResume.setCurrentCompany(parseResult.getCurrentCompany());
        parsedResume.setPhone(parseResult.getPhone());
        parsedResume.setLocation(parseResult.getLocation());
        parsedResume.setLinkedinUrl(parseResult.getLinkedinUrl());
        parsedResume.setGithubUrl(parseResult.getGithubUrl());
        parsedResume.setPortfolioUrl(parseResult.getPortfolioUrl());
        parsedResume.setTotalExperienceYears(parseResult.getTotalExperienceYears());
        parsedResume.setParseError(null);
        parsedResume.setParsedAt(LocalDateTime.now());
        parsedResume.setParsingStatus(ParsingStatus.PARSED);

        addSkills(parsedResume, parseResult.getSkills());
        addEducations(parsedResume, parseResult.getEducations());
        addExperiences(parsedResume, parseResult.getExperiences());
    }

    private void addSkills(ParsedResume parsedResume, List<ParsedSkillData> skills) {
        if (skills == null || skills.isEmpty()) {
            return;
        }

        for (ParsedSkillData skillData : skills) {
            ParsedResumeSkill skill = new ParsedResumeSkill();
            skill.setSkillName(skillData.getSkillName());
            skill.setNormalizedSkillName(skillData.getNormalizedSkillName());
            parsedResume.addSkill(skill);
        }
    }

    private void addEducations(ParsedResume parsedResume, List<ParsedEducationData> educations) {
        if (educations == null || educations.isEmpty()) {
            return;
        }

        for (ParsedEducationData educationData : educations) {
            ParsedResumeEducation education = new ParsedResumeEducation();
            education.setInstitutionName(educationData.getInstitutionName());
            education.setDegree(educationData.getDegree());
            education.setFieldOfStudy(educationData.getFieldOfStudy());
            education.setStartYear(educationData.getStartYear());
            education.setEndYear(educationData.getEndYear());
            education.setGrade(educationData.getGrade());
            parsedResume.addEducation(education);
        }
    }

    private void addExperiences(ParsedResume parsedResume, List<ParsedExperienceData> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return;
        }

        for (ParsedExperienceData experienceData : experiences) {
            ParsedResumeExperience experience = new ParsedResumeExperience();
            experience.setCompanyName(experienceData.getCompanyName());
            experience.setJobTitle(experienceData.getJobTitle());
            experience.setStartDate(experienceData.getStartDate());
            experience.setEndDate(experienceData.getEndDate());
            experience.setCurrentlyWorking(experienceData.getCurrentlyWorking());
            experience.setDescription(experienceData.getDescription());
            parsedResume.addExperience(experience);
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