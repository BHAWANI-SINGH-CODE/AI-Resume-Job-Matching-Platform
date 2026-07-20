package com.bhawanisingh.airesume.resumeparse.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedExperienceData {

    private String companyName;

    private String jobTitle;

    private String startDate;

    private String endDate;

    private Boolean currentlyWorking;

    private String description;

}