package com.bhawanisingh.airesume.resumeparse.entity;

import com.bhawanisingh.airesume.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "parsed_resume_educations")
public class ParsedResumeEducation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "parsed_resume_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_parsed_resume_education_parsed_resume")
    )
    private ParsedResume parsedResume;

    @Column(name = "institution_name", length = 200)
    private String institutionName;

    @Column(name = "degree", length = 150)
    private String degree;

    @Column(name = "field_of_study", length = 150)
    private String fieldOfStudy;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "grade", length = 50)
    private String grade;

    public ParsedResumeEducation() {
    }

    public ParsedResume getParsedResume() {
        return parsedResume;
    }

    public void setParsedResume(ParsedResume parsedResume) {
        this.parsedResume = parsedResume;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}