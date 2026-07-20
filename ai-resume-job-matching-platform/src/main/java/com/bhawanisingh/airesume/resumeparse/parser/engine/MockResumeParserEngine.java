package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedEducationData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedSkillData;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockResumeParserEngine implements ResumeParserEngine {

    @Override
    public ResumeParseResult parseResume(Resume resume) {

        ResumeParseResult result = new ResumeParseResult();

        result.setRawText("""
                Bhawani Singh

                Java Backend Developer

                Email : bbhawani@example.com

                Phone : +91-9999999999

                Skills :
                Java
                Spring Boot
                Microservices
                MySQL
                Docker
                AWS
                """);

        result.setProfessionalSummary(
                "Experienced Java Backend Developer with Spring Boot knowledge."
        );

        result.setCurrentTitle("Java Backend Developer");

        result.setCurrentCompany("OpenAI Technologies");

        result.setPhone("+91-9999999999");

        result.setLocation("Jaipur");

        result.setLinkedinUrl(
                "https://linkedin.com/in/bhawani"
        );

        result.setGithubUrl(
                "https://github.com/bhawani"
        );

        result.setPortfolioUrl(
                "https://bhawani.dev"
        );

        result.setTotalExperienceYears(
                BigDecimal.valueOf(3.5)
        );

        List<ParsedSkillData> skills = new ArrayList<>();

        skills.add(new ParsedSkillData(
                "Java",
                "JAVA"
        ));

        skills.add(new ParsedSkillData(
                "Spring Boot",
                "SPRING_BOOT"
        ));

        skills.add(new ParsedSkillData(
                "Microservices",
                "MICROSERVICES"
        ));

        skills.add(new ParsedSkillData(
                "Docker",
                "DOCKER"
        ));

        skills.add(new ParsedSkillData(
                "AWS",
                "AWS"
        ));

        result.setSkills(skills);

        List<ParsedEducationData> educations = new ArrayList<>();

        ParsedEducationData education = new ParsedEducationData();

        education.setInstitutionName(
                "Rajasthan Technical University"
        );

        education.setDegree("Bachelor of Technology");

        education.setFieldOfStudy(
                "Computer Science"
        );

        education.setStartYear(2020);

        education.setEndYear(2024);

        education.setGrade("8.5 CGPA");

        educations.add(education);

        result.setEducations(educations);

        List<ParsedExperienceData> experiences = new ArrayList<>();

        ParsedExperienceData experience = new ParsedExperienceData();

        experience.setCompanyName("OpenAI Technologies");
        experience.setJobTitle("Backend Developer");

        experience.setStartDate("Jan 2022");
        experience.setEndDate("Present");

        experience.setCurrentlyWorking(true);

        experience.setDescription(
                "Worked on Spring Boot APIs."
        );

        experiences.add(experience);

        result.setExperiences(experiences);

        return result;
    }

}