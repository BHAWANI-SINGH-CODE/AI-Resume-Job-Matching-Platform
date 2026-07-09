package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import com.bhawanisingh.airesume.resumeparse.parser.extractor.ResumeTextExtractor;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedSkillData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockResumeParserEngine implements ResumeParserEngine {

    private final ResumeTextExtractor resumeTextExtractor;

    public MockResumeParserEngine(ResumeTextExtractor resumeTextExtractor) {
        this.resumeTextExtractor = resumeTextExtractor;
    }

    @Override
    public ResumeParseResult parse(Resume resume) {
        if (resume == null) {
            throw new ResumeParsingException("Resume is required for parsing");
        }

        String extractedText = resumeTextExtractor.extractText(resume);

        ResumeParseResult result = new ResumeParseResult();
        result.setRawText(extractedText);
        result.setProfessionalSummary("Mock parsed summary for resume: " + resume.getOriginalFileName());
        result.setCurrentTitle("Not parsed yet");
        result.setCurrentCompany("Not parsed yet");
        result.setPhone(null);
        result.setLocation(null);
        result.setLinkedinUrl(null);
        result.setGithubUrl(null);
        result.setPortfolioUrl(null);
        result.setTotalExperienceYears(null);
        result.setSkills(buildMockSkills());
        result.setEducations(new ArrayList<>());
        result.setExperiences(new ArrayList<>());

        return result;
    }

    private List<ParsedSkillData> buildMockSkills() {
        List<ParsedSkillData> skills = new ArrayList<>();

        skills.add(new ParsedSkillData("Java", "java"));
        skills.add(new ParsedSkillData("Spring Boot", "spring boot"));
        skills.add(new ParsedSkillData("SQL", "sql"));

        return skills;
    }
}