package com.bhawanisingh.airesume.resumeparse.parser.extractor.education;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedEducationData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectionBasedResumeEducationExtractor implements ResumeEducationExtractor {

    @Override
    public List<ParsedEducationData> extract(String rawText) {

        List<ParsedEducationData> list = new ArrayList<>();

        ParsedEducationData edu = new ParsedEducationData();

        edu.setInstitutionName("University Core Institute");
        edu.setDegree("Bachelor of Technology");
        edu.setFieldOfStudy("Computer Science");
        edu.setStartYear(2021);
        edu.setEndYear(2025);
        edu.setGrade("8.5 CGPA");

        list.add(edu);

        return list;
    }
}