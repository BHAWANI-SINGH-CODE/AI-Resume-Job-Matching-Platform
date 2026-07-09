package com.bhawanisingh.airesume.resumeparse.entity;

import com.bhawanisingh.airesume.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "parsed_resume_skills")
public class ParsedResumeSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "parsed_resume_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_parsed_resume_skill_parsed_resume")
    )
    private ParsedResume parsedResume;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;

    @Column(name = "normalized_skill_name", length = 100)
    private String normalizedSkillName;

    public ParsedResumeSkill() {
    }

    public ParsedResume getParsedResume() {
        return parsedResume;
    }

    public void setParsedResume(ParsedResume parsedResume) {
        this.parsedResume = parsedResume;
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