package com.bhawanisingh.airesume.resumeparse.parser.extractor.skill;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedSkillData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class KeywordResumeSkillExtractor implements ResumeSkillExtractor {

    private static final Map<String, String> SUPPORTED_SKILLS = buildSupportedSkills();

    @Override
    public List<ParsedSkillData> extractSkills(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return buildFallbackSkills();
        }

        String normalizedText = normalizeText(rawText);

        Map<String, ParsedSkillData> extractedSkills = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : SUPPORTED_SKILLS.entrySet()) {
            String normalizedSkill = entry.getKey();
            String displaySkill = entry.getValue();

            if (containsSkill(normalizedText, normalizedSkill)) {
                extractedSkills.putIfAbsent(
                        normalizedSkill,
                        new ParsedSkillData(displaySkill, normalizedSkill)
                );
            }
        }

        if (extractedSkills.isEmpty()) {
            return buildFallbackSkills();
        }

        return new ArrayList<>(extractedSkills.values());
    }

    private boolean containsSkill(String normalizedText, String normalizedSkill) {
        if (!StringUtils.hasText(normalizedText) || !StringUtils.hasText(normalizedSkill)) {
            return false;
        }

        String regexSafeSkill = normalizedSkill.replace("+", "\\+");
        String regex = "(?<![a-z0-9])" + regexSafeSkill + "(?![a-z0-9])";

        return normalizedText.matches("(?s).*" + regex + ".*");
    }

    private String normalizeText(String rawText) {
        return rawText.toLowerCase(Locale.ROOT)
                .replaceAll("[\\r\\n\\t]+", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private List<ParsedSkillData> buildFallbackSkills() {
        List<ParsedSkillData> fallbackSkills = new ArrayList<>();
        fallbackSkills.add(new ParsedSkillData("Resume Parsing", "resume parsing"));
        fallbackSkills.add(new ParsedSkillData("Text Extraction", "text extraction"));
        return fallbackSkills;
    }

    private static Map<String, String> buildSupportedSkills() {
        Map<String, String> skills = new LinkedHashMap<>();

        skills.put("java", "Java");
        skills.put("spring", "Spring");
        skills.put("spring boot", "Spring Boot");
        skills.put("spring security", "Spring Security");
        skills.put("hibernate", "Hibernate");
        skills.put("jpa", "JPA");
        skills.put("rest api", "REST API");
        skills.put("microservices", "Microservices");

        skills.put("mysql", "MySQL");
        skills.put("postgresql", "PostgreSQL");
        skills.put("sql", "SQL");
        skills.put("mongodb", "MongoDB");
        skills.put("redis", "Redis");

        skills.put("html", "HTML");
        skills.put("css", "CSS");
        skills.put("javascript", "JavaScript");
        skills.put("typescript", "TypeScript");
        skills.put("react", "React");
        skills.put("angular", "Angular");
        skills.put("bootstrap", "Bootstrap");
        skills.put("tailwind css", "Tailwind CSS");

        skills.put("python", "Python");
        skills.put("c", "C");
        skills.put("c++", "C++");

        skills.put("git", "Git");
        skills.put("github", "GitHub");
        skills.put("docker", "Docker");
        skills.put("aws", "AWS");
        skills.put("postman", "Postman");
        skills.put("maven", "Maven");

        skills.put("data structures", "Data Structures");
        skills.put("algorithms", "Algorithms");
        skills.put("oop", "OOP");
        skills.put("problem solving", "Problem Solving");

        return skills;
    }
}