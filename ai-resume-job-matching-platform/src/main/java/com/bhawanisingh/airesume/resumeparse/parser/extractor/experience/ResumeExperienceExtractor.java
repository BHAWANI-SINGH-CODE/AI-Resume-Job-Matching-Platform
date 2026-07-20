package com.bhawanisingh.airesume.resumeparse.parser.extractor.experience;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import java.util.List;

public interface ResumeExperienceExtractor {
    List<ParsedExperienceData> extract(String rawText);
}