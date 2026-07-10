package com.bhawanisingh.airesume.resumeparse.parser.extractor.field;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexResumeFieldExtractor implements ResumeFieldExtractor {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(?<!\\d)(?:\\+91[-\\s]?)?[6-9]\\d{9}(?!\\d)|(?<!\\d)(?:\\+\\d{1,3}[-\\s]?)?(?:\\(?\\d{2,5}\\)?[-\\s]?)?\\d{3,5}[-\\s]?\\d{3,5}(?!\\d)");

    private static final Pattern LINKEDIN_PATTERN =
            Pattern.compile("(https?://)?(www\\.)?linkedin\\.com/[A-Za-z0-9\\-_/?.=&%]+", Pattern.CASE_INSENSITIVE);

    private static final Pattern GITHUB_PATTERN =
            Pattern.compile("(https?://)?(www\\.)?github\\.com/[A-Za-z0-9\\-_/?.=&%]+", Pattern.CASE_INSENSITIVE);

    private static final Pattern URL_PATTERN =
            Pattern.compile("(https?://[A-Za-z0-9./?=&_%#\\-]+)|(www\\.[A-Za-z0-9./?=&_%#\\-]+)", Pattern.CASE_INSENSITIVE);

    private static final List<String> COMMON_TITLES = List.of(
            "software engineer",
            "software developer",
            "java developer",
            "backend developer",
            "backend engineer",
            "full stack developer",
            "fullstack developer",
            "spring boot developer",
            "web developer",
            "frontend developer",
            "data analyst",
            "machine learning engineer",
            "devops engineer",
            "android developer",
            "intern"
    );

    private static final List<String> COMPANY_HINTS = List.of(
            " at ",
            " @ "
    );

    @Override
    public String extractProfessionalSummary(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        String normalized = normalizeWhitespace(rawText);
        if (!StringUtils.hasText(normalized)) {
            return null;
        }

        int maxLength = Math.min(normalized.length(), 300);
        String summary = normalized.substring(0, maxLength).trim();

        if (normalized.length() > 300) {
            summary += "...";
        }

        return summary;
    }

    @Override
    public String extractPhone(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        Matcher matcher = PHONE_PATTERN.matcher(rawText);
        while (matcher.find()) {
            String phone = cleanPhone(matcher.group());
            if (StringUtils.hasText(phone)) {
                return phone;
            }
        }

        return null;
    }

    @Override
    public String extractLinkedinUrl(String rawText) {
        return extractFirstMatchedUrl(rawText, LINKEDIN_PATTERN);
    }

    @Override
    public String extractGithubUrl(String rawText) {
        return extractFirstMatchedUrl(rawText, GITHUB_PATTERN);
    }

    @Override
    public String extractPortfolioUrl(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        Matcher matcher = URL_PATTERN.matcher(rawText);
        while (matcher.find()) {
            String url = normalizeUrl(matcher.group());

            if (!StringUtils.hasText(url)) {
                continue;
            }

            String lower = url.toLowerCase(Locale.ROOT);

            if (lower.contains("linkedin.com") || lower.contains("github.com")) {
                continue;
            }

            return url;
        }

        return null;
    }

    @Override
    public String extractLocation(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        List<String> lines = getMeaningfulLines(rawText);

        for (String line : lines) {
            String lower = line.toLowerCase(Locale.ROOT);

            if (lower.contains("linkedin")
                    || lower.contains("github")
                    || lower.contains("http")
                    || lower.contains("@")
                    || containsPhoneNumber(line)) {
                continue;
            }

            if (looksLikeLocation(line)) {
                return line.trim();
            }
        }

        return null;
    }

    @Override
    public String extractCurrentTitle(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        String normalized = normalizeWhitespace(rawText).toLowerCase(Locale.ROOT);

        for (String title : COMMON_TITLES) {
            if (normalized.contains(title)) {
                return toTitleCase(title);
            }
        }

        List<String> lines = getMeaningfulLines(rawText);
        for (String line : lines) {
            String lower = line.toLowerCase(Locale.ROOT);

            for (String title : COMMON_TITLES) {
                if (lower.contains(title)) {
                    return toTitleCase(title);
                }
            }
        }

        return null;
    }

    @Override
    public String extractCurrentCompany(String rawText) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        List<String> lines = getMeaningfulLines(rawText);

        for (String line : lines) {
            String normalizedLine = " " + line.trim().toLowerCase(Locale.ROOT) + " ";

            for (String companyHint : COMPANY_HINTS) {
                int index = normalizedLine.indexOf(companyHint);
                if (index != -1) {
                    String originalLine = line.trim();
                    int actualIndex = originalLine.toLowerCase(Locale.ROOT).indexOf(companyHint.trim());
                    if (actualIndex != -1) {
                        String companyPart = originalLine.substring(actualIndex + companyHint.trim().length()).trim();
                        if (StringUtils.hasText(companyPart) && companyPart.length() <= 100) {
                            return companyPart;
                        }
                    }
                }
            }
        }

        return null;
    }

    private String extractFirstMatchedUrl(String rawText, Pattern pattern) {
        if (!StringUtils.hasText(rawText)) {
            return null;
        }

        Matcher matcher = pattern.matcher(rawText);
        if (matcher.find()) {
            return normalizeUrl(matcher.group());
        }

        return null;
    }

    private String normalizeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }

        String trimmed = url.trim();

        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }

        return "https://" + trimmed;
    }

    private String cleanPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }

        return phone.trim().replaceAll("\\s+", " ");
    }

    private boolean containsPhoneNumber(String line) {
        return PHONE_PATTERN.matcher(line).find();
    }

    private boolean looksLikeLocation(String line) {
        if (!StringUtils.hasText(line)) {
            return false;
        }

        String cleaned = line.trim();
        if (cleaned.length() < 3 || cleaned.length() > 80) {
            return false;
        }

        if (cleaned.matches(".*\\d{5,}.*")) {
            return false;
        }

        if (cleaned.contains(",")) {
            return true;
        }

        String lower = cleaned.toLowerCase(Locale.ROOT);
        return lower.contains("india")
                || lower.contains("delhi")
                || lower.contains("jaipur")
                || lower.contains("mumbai")
                || lower.contains("bangalore")
                || lower.contains("pune")
                || lower.contains("hyderabad")
                || lower.contains("kota")
                || lower.contains("remote");
    }

    private List<String> getMeaningfulLines(String rawText) {
        List<String> lines = new ArrayList<>();

        if (!StringUtils.hasText(rawText)) {
            return lines;
        }

        String[] splitLines = rawText.split("\\r?\\n");
        for (String line : splitLines) {
            if (!StringUtils.hasText(line)) {
                continue;
            }

            String trimmed = line.trim();
            if (trimmed.length() < 2) {
                continue;
            }

            lines.add(trimmed);
        }

        return lines;
    }

    private String normalizeWhitespace(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.replaceAll("\\s+", " ").trim();
    }

    private String toTitleCase(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }

        String[] parts = value.split("\\s+");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            builder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }

        return builder.toString().trim();
    }
}