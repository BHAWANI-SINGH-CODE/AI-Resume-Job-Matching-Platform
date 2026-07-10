package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.ResumeTextExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.education.ResumeEducationExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.experience.ResumeExperienceExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.field.ResumeFieldExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.skill.ResumeSkillExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
import com.bhawanisingh.airesume.resumeparse.parser.util.ResumeExperienceCalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MockResumeParserEngine implements ResumeParserEngine {

    private final ResumeTextExtractor resumeTextExtractor;
    private final ResumeFieldExtractor resumeFieldExtractor;
    private final ResumeSkillExtractor resumeSkillExtractor;
    private final ResumeEducationExtractor resumeEducationExtractor;
    private final ResumeExperienceExtractor resumeExperienceExtractor;
    private final ResumeExperienceCalculationUtil resumeExperienceCalculationUtil;

    @Override
    public ResumeParseResult parse(Resume resume) {
        String extractedText = resumeTextExtractor.extractText(resume);

        ResumeParseResult result = new ResumeParseResult();
        result.setRawText(extractedText);
        result.setProfessionalSummary(resumeFieldExtractor.extractProfessionalSummary(extractedText));
        result.setPhone(resumeFieldExtractor.extractPhone(extractedText));
        result.setLocation(resumeFieldExtractor.extractLocation(extractedText));
        result.setLinkedinUrl(resumeFieldExtractor.extractLinkedinUrl(extractedText));
        result.setGithubUrl(resumeFieldExtractor.extractGithubUrl(extractedText));
        result.setPortfolioUrl(resumeFieldExtractor.extractPortfolioUrl(extractedText));
        result.setSkills(resumeSkillExtractor.extractSkills(extractedText));
        result.setEducations(resumeEducationExtractor.extractEducations(extractedText));

        List<ParsedExperienceData> experiences = resumeExperienceExtractor.extractExperiences(extractedText);
        result.setExperiences(experiences);

        BigDecimal totalExperienceYears = resumeExperienceCalculationUtil.calculateTotalExperienceYears(experiences);
        result.setTotalExperienceYears(totalExperienceYears);

        ParsedExperienceData currentOrLatestExperience =
                resumeExperienceCalculationUtil.findCurrentOrLatestExperience(experiences);

        String extractedCurrentTitle = resumeFieldExtractor.extractCurrentTitle(extractedText);
        String extractedCurrentCompany = resumeFieldExtractor.extractCurrentCompany(extractedText);

        result.setCurrentTitle(resolveCurrentTitle(extractedCurrentTitle, currentOrLatestExperience));
        result.setCurrentCompany(resolveCurrentCompany(extractedCurrentCompany, currentOrLatestExperience));

        return result;
    }

    private String resolveCurrentTitle(String extractedCurrentTitle, ParsedExperienceData currentOrLatestExperience) {
        if (StringUtils.hasText(extractedCurrentTitle)) {
            return extractedCurrentTitle;
        }

        if (currentOrLatestExperience != null && StringUtils.hasText(currentOrLatestExperience.getJobTitle())) {
            return currentOrLatestExperience.getJobTitle();
        }

        return null;
    }

    private String resolveCurrentCompany(String extractedCurrentCompany, ParsedExperienceData currentOrLatestExperience) {
        if (StringUtils.hasText(extractedCurrentCompany)) {
            return extractedCurrentCompany;
        }

        if (currentOrLatestExperience != null && StringUtils.hasText(currentOrLatestExperience.getCompanyName())) {
            return currentOrLatestExperience.getCompanyName();
        }

        return null;
    }
}