package com.bhawanisingh.airesume.resumeparse.parser.extractor.field;

public interface ResumeFieldExtractor {

    String extractProfessionalSummary(String rawText);

    String extractPhone(String rawText);

    String extractLinkedinUrl(String rawText);

    String extractGithubUrl(String rawText);

    String extractPortfolioUrl(String rawText);

    String extractLocation(String rawText);

    String extractCurrentTitle(String rawText);

    String extractCurrentCompany(String rawText);
}