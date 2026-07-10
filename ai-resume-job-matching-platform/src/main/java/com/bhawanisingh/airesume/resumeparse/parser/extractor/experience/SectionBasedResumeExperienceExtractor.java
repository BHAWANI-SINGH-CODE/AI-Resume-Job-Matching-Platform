package com.bhawanisingh.airesume.resumeparse.parser.extractor.experience;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SectionBasedResumeExperienceExtractor implements ResumeExperienceExtractor {

    private static final List<String> EXPERIENCE_HEADERS = List.of(
            "experience",
            "work experience",
            "professional experience",
            "employment history",
            "employment"
    );

    private static final List<String> SECTION_STOP_HEADERS = List.of(
            "education",
            "projects",
            "skills",
            "technical skills",
            "certifications",
            "achievements",
            "summary",
            "profile"
    );

    private static final Pattern DATE_RANGE_PATTERN = Pattern.compile(
            "((?:jan|feb|mar|apr|may|jun|jul|aug|sep|sept|oct|nov|dec)[a-z]*\\s+\\d{4}|\\d{4})\\s*[-–—to]+\\s*((?:present|current|till date)|(?:jan|feb|mar|apr|may|jun|jul|aug|sep|sept|oct|nov|dec)[a-z]*\\s+\\d{4}|\\d{4})",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public List<ParsedExperienceData> extractExperiences(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return new ArrayList<>();
        }

        List<String> experienceLines = extractExperienceSectionLines(rawText);
        if (experienceLines.isEmpty()) {
            return new ArrayList<>();
        }

        return buildExperienceEntries(experienceLines);
    }

    private List<String> extractExperienceSectionLines(String rawText) {
        List<String> lines = splitLines(rawText);
        List<String> sectionLines = new ArrayList<>();

        boolean insideExperienceSection = false;

        for (String line : lines) {
            String normalizedLine = normalize(line);

            if (!insideExperienceSection && isExperienceHeader(normalizedLine)) {
                insideExperienceSection = true;
                continue;
            }

            if (insideExperienceSection && isStopHeader(normalizedLine)) {
                break;
            }

            if (insideExperienceSection && StringUtils.hasText(line)) {
                sectionLines.add(line.trim());
            }
        }

        return sectionLines;
    }

    private List<ParsedExperienceData> buildExperienceEntries(List<String> experienceLines) {
        List<ParsedExperienceData> experiences = new ArrayList<>();

        for (String line : experienceLines) {
            if (!StringUtils.hasText(line)) {
                continue;
            }

            ParsedExperienceData experience = parseExperienceLine(line);
            if (experience != null) {
                experiences.add(experience);
            }
        }

        return experiences;
    }

    private ParsedExperienceData parseExperienceLine(String line) {
        String cleanedLine = line.trim();
        if (cleanedLine.length() < 3) {
            return null;
        }

        ParsedExperienceData experience = new ParsedExperienceData();

        String[] parts = cleanedLine.split("\\s*\\|\\s*");
        if (parts.length == 1) {
            parts = cleanedLine.split("\\s{2,}");
        }

        String jobTitle = null;
        String companyName = null;
        String startDate = null;
        String endDate = null;
        String description = null;

        if (parts.length >= 1) {
            jobTitle = emptyToNull(parts[0]);
        }
        if (parts.length >= 2) {
            companyName = emptyToNull(parts[1]);
        }
        if (parts.length >= 3) {
            String durationPart = parts[2].trim();
            DateRange dateRange = extractDateRange(durationPart);
            if (dateRange != null) {
                startDate = dateRange.startDate();
                endDate = dateRange.endDate();
            } else {
                startDate = emptyToNull(durationPart);
            }
        }
        if (parts.length >= 4) {
            description = emptyToNull(parts[3]);
        }

        if (parts.length < 3) {
            DateRange lineDateRange = extractDateRange(cleanedLine);
            if (lineDateRange != null) {
                startDate = startDate == null ? lineDateRange.startDate() : startDate;
                endDate = endDate == null ? lineDateRange.endDate() : endDate;
            }
        }

        experience.setJobTitle(jobTitle);
        experience.setCompanyName(companyName);
        experience.setStartDate(startDate);
        experience.setEndDate(endDate);
        experience.setCurrentlyWorking(isCurrentRole(cleanedLine, endDate));
        experience.setDescription(description);

        if (isExperienceEntryEmpty(experience)) {
            return null;
        }

        return experience;
    }

    private DateRange extractDateRange(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        Matcher matcher = DATE_RANGE_PATTERN.matcher(value);
        if (!matcher.find()) {
            return null;
        }

        return new DateRange(
                normalizeDateToken(matcher.group(1)),
                normalizeDateToken(matcher.group(2))
        );
    }

    private boolean isExperienceHeader(String normalizedLine) {
        return EXPERIENCE_HEADERS.contains(normalizedLine);
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

    private boolean isCurrentRole(String line, String endDate) {
        if (StringUtils.hasText(endDate)) {
            String normalizedEndDate = endDate.toLowerCase(Locale.ROOT);
            if (normalizedEndDate.contains("present")
                    || normalizedEndDate.contains("current")
                    || normalizedEndDate.contains("till date")) {
                return true;
            }
        }

        String normalized = line.toLowerCase(Locale.ROOT);
        return normalized.contains("present")
                || normalized.contains("current")
                || normalized.contains("till date");
    }

    private String normalizeDateToken(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim()
                .replaceAll("\\s+", " ")
                .replace("Sept", "Sep")
                .replace("sept", "sep");
    }

    private String emptyToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private boolean isExperienceEntryEmpty(ParsedExperienceData experience) {
        return !StringUtils.hasText(experience.getJobTitle())
                && !StringUtils.hasText(experience.getCompanyName())
                && !StringUtils.hasText(experience.getStartDate())
                && !StringUtils.hasText(experience.getEndDate())
                && !StringUtils.hasText(experience.getDescription());
    }

    private record DateRange(String startDate, String endDate) {
    }
}