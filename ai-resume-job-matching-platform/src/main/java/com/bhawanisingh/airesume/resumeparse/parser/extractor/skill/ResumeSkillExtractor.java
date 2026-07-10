package com.bhawanisingh.airesume.resumeparse.parser.extractor.skill;

import com.bhawanisingh.airesume.resumeparse.parser.model.ParsedSkillData;

import java.util.List;

public interface ResumeSkillExtractor {

    List<ParsedSkillData> extractSkills(String rawText);
}