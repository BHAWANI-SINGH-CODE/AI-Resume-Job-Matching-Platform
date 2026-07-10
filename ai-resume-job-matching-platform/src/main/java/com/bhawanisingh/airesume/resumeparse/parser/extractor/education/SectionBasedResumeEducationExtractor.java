package com.bhawanisingh.airesume.resumeparse.parser.extractor.education;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedEducationData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SectionBasedResumeEducationExtractor implements ResumeEducationExtractor {

    private static final List<String> EDUCATION_HEADERS = List.of(
            "education",
            "academic qualification",
            "academic qualifications",
            "qualification",
            "qualifications"
    );

    private static final List<String> SECTION_STOP_HEADERS = List.of(
            "experience",
            "work experience",
            "employment",
            "projects",
            "skills",
            "technical skills",
            "certifications",
            "achievements",
            "summary",
            "profile"
    );

    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(19|20)\\d{2}\\b");

    @Override
    public List<ParsedEducationData> extractEducations(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return new ArrayList<>();
        }

        List<String> educationLines = extractEducationSectionLines(rawText);
        if (educationLines.isEmpty()) {
            return new ArrayList<>();
        }

        return buildEducationEntries(educationLines);
    }

    private List<String> extractEducationSectionLines(String rawText) {
        List<String> lines = splitLines(rawText);
        List<String> sectionLines = new ArrayList<>();

        boolean insideEducationSection = false;

        for (String line : lines) {
            String normalizedLine = normalize(line);

            if (!insideEducationSection && isEducationHeader(normalizedLine)) {
                insideEducationSection = true;
                continue;
            }

            if (insideEducationSection && isStopHeader(normalizedLine)) {
                break;
            }

            if (insideEducationSection && StringUtils.hasText(line)) {
                sectionLines.add(line.trim());
            }
        }

        return sectionLines;
    }

    private List<ParsedEducationData> buildEducationEntries(List<String> educationLines) {
        List<ParsedEducationData> educations = new ArrayList<>();

        for (String line : educationLines) {
            if (!StringUtils.hasText(line)) {
                continue;
            }

            ParsedEducationData education = parseEducationLine(line);
            if (education != null) {
                educations.add(education);
            }
        }

        return educations;
    }

    private ParsedEducationData parseEducationLine(String line) {
        String cleanedLine = line.trim();
        if (cleanedLine.length() < 3) {
            return null;
        }

        ParsedEducationData education = new ParsedEducationData();

        List<Integer> years = extractYears(cleanedLine);
        if (!years.isEmpty()) {
            education.setStartYear(years.get(0));
            if (years.size() > 1) {
                education.setEndYear(years.get(1));
            }
        }

        String[] parts = cleanedLine.split("\\s*\\|\\s*|\\s*-\\s*|\\s*,\\s*");
        if (parts.length == 0) {
            return null;
        }

        String institution = null;
        String degree = null;
        String fieldOfStudy = null;

        for (String part : parts) {
            String trimmed = part.trim();
            if (!StringUtils.hasText(trimmed)) {
                continue;
            }

            String lower = trimmed.toLowerCase(Locale.ROOT);

            if (institution == null && looksLikeInstitution(lower)) {
                institution = trimmed;
                continue;
            }

            if (degree == null && looksLikeDegree(lower)) {
                degree = trimmed;
                continue;
            }

            if (fieldOfStudy == null && looksLikeFieldOfStudy(lower)) {
                fieldOfStudy = trimmed;
            }
        }

        if (institution == null && parts.length > 0) {
            institution = parts[0].trim();
        }

        if (degree == null && parts.length > 1) {
            degree = parts[1].trim();
        }

        education.setInstitutionName(emptyToNull(institution));
        education.setDegree(emptyToNull(degree));
        education.setFieldOfStudy(emptyToNull(fieldOfStudy));

        if (isEducationEntryEmpty(education)) {
            return null;
        }

        return education;
    }

    private boolean isEducationHeader(String normalizedLine) {
        return EDUCATION_HEADERS.contains(normalizedLine);
    }

    private boolean isStopHeader(String normalizedLine) {
        return SECTION_STOP_HEADERS.contains(normalizedLine);
    }

    private List<String> splitLines(String rawText) {
        String[] rawLines = rawText.split("\\r?\\n");
        List<String> lines = new ArrayList<>();

        for (String rawLine : rawLines) {
            if (!StringUtils.hasText(rawLine)) {
                continue;
            }

            String trimmed = rawLine.trim();
            if (trimmed.length() < 2) {
                continue;
            }

            lines.add(trimmed);
        }

        return lines;
    }

    private String normalize(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    private List<Integer> extractYears(String line) {
        List<Integer> years = new ArrayList<>();
        Matcher matcher = YEAR_PATTERN.matcher(line);

        while (matcher.find()) {
            years.add(Integer.parseInt(matcher.group()));
        }

        return years;
    }

    private boolean looksLikeInstitution(String value) {
        return value.contains("college")
                || value.contains("university")
                || value.contains("institute")
                || value.contains("school")
                || value.contains("academy");
    }

    private boolean looksLikeDegree(String value) {
        return value.contains("b.tech")
                || value.contains("btech")
                || value.contains("m.tech")
                || value.contains("mtech")
                || value.contains("b.e")
                || value.contains("be ")
                || value.contains("bachelor")
                || value.contains("master")
                || value.contains("diploma")
                || value.contains("mba")
                || value.contains("bca")
                || value.contains("mca");
    }

    private boolean looksLikeFieldOfStudy(String value) {
        return value.contains("computer")
                || value.contains("information technology")
                || value.contains("software")
                || value.contains("mechanical")
                || value.contains("civil")
                || value.contains("electrical")
                || value.contains("electronics")
                || value.contains("science");
    }

    private String emptyToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private boolean isEducationEntryEmpty(ParsedEducationData education) {
        return !StringUtils.hasText(education.getInstitutionName())
                && !StringUtils.hasText(education.getDegree())
                && !StringUtils.hasText(education.getFieldOfStudy())
                && education.getStartYear() == null
                && education.getEndYear() == null;
    }
}