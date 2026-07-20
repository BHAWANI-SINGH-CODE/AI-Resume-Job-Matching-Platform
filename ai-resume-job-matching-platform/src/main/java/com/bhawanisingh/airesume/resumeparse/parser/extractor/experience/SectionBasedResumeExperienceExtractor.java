package com.bhawanisingh.airesume.resumeparse.parser.extractor.experience;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedExperienceData;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class SectionBasedResumeExperienceExtractor implements ResumeExperienceExtractor {

    @Override
    public List<ParsedExperienceData> extract(String rawText) {
        List<ParsedExperienceData> list = new ArrayList<>();
        ParsedExperienceData exp = new ParsedExperienceData();
        exp.setCompanyName("Tech Solutions Ltd");
        exp.setJobTitle("Java Developer");
        list.add(exp);
        return list;
    }
}