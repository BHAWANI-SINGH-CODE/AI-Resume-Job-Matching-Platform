package com.bhawanisingh.airesume.resumeparse.parser.extractor.education;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedEducationData;

import java.util.List;

public interface ResumeEducationExtractor {

    List<ParsedEducationData> extractEducations(String rawText);
}