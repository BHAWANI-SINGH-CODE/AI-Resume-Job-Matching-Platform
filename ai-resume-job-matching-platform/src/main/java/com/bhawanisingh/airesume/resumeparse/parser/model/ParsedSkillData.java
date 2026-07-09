package com.bhawanisingh.airesume.resumeparse.parser.model;

public class ParsedSkillData {

    private String skillName;
    private String normalizedSkillName;

    public ParsedSkillData() {
    }

    public ParsedSkillData(String skillName, String normalizedSkillName) {
        this.skillName = skillName;
        this.normalizedSkillName = normalizedSkillName;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getNormalizedSkillName() {
        return normalizedSkillName;
    }

    public void setNormalizedSkillName(String normalizedSkillName) {
        this.normalizedSkillName = normalizedSkillName;
    }
}