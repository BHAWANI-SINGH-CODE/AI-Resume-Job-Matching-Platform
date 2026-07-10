package com.bhawanisingh.airesume.resumeparse.parser.util;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Component
public class ResumeExperienceCalculationUtil {

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    private static final DateTimeFormatter FULL_MONTH_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

    public BigDecimal calculateTotalExperienceYears(List<ParsedExperienceData> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return null;
        }

        long totalMonths = 0L;

        for (ParsedExperienceData experience : experiences) {
            Long months = calculateExperienceMonths(experience);
            if (months != null && months > 0) {
                totalMonths += months;
            }
        }

        if (totalMonths <= 0) {
            return null;
        }

        return BigDecimal.valueOf(totalMonths)
                .divide(BigDecimal.valueOf(12), 1, RoundingMode.HALF_UP);
    }

    public ParsedExperienceData findCurrentOrLatestExperience(List<ParsedExperienceData> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return null;
        }

        for (ParsedExperienceData experience : experiences) {
            if (Boolean.TRUE.equals(experience.getCurrentlyWorking())) {
                return experience;
            }
        }

        return experiences.get(0);
    }

    private Long calculateExperienceMonths(ParsedExperienceData experience) {
        if (experience == null) {
            return null;
        }

        YearMonth start = parseToYearMonth(experience.getStartDate());
        if (start == null) {
            return null;
        }

        YearMonth end;
        if (Boolean.TRUE.equals(experience.getCurrentlyWorking())) {
            end = YearMonth.now();
        } else {
            end = parseToYearMonth(experience.getEndDate());
        }

        if (end == null || end.isBefore(start)) {
            return null;
        }

        long monthDifference = (end.getYear() - start.getYear()) * 12L
                + (end.getMonthValue() - start.getMonthValue());

        return Math.max(monthDifference + 1, 0);
    }

    private YearMonth parseToYearMonth(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        String normalized = normalizeDateToken(value);

        if ("present".equals(normalized) || "current".equals(normalized) || "till date".equals(normalized)) {
            return YearMonth.now();
        }

        try {
            return YearMonth.parse(toMonthYearFormat(normalized), MONTH_YEAR_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return YearMonth.parse(toMonthYearFormat(normalized), FULL_MONTH_YEAR_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        if (normalized.matches("\\d{4}")) {
            return YearMonth.of(Integer.parseInt(normalized), 1);
        }

        return null;
    }

    private String normalizeDateToken(String value) {
        return value.trim()
                .replaceAll("\\s+", " ")
                .replace("–", "-")
                .replace("—", "-")
                .toLowerCase(Locale.ENGLISH);
    }

    private String toMonthYearFormat(String normalized) {
        String[] parts = normalized.split(" ");
        if (parts.length != 2) {
            return normalized;
        }

        String month = capitalize(parts[0]);
        String year = parts[1];
        return month + " " + year;
    }

    private String capitalize(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }

        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase(Locale.ENGLISH);
    }
}