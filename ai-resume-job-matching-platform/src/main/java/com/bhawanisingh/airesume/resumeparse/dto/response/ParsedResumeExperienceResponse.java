package com.bhawanisingh.airesume.resumeparse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResumeExperienceResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private String startDate;
    private String endDate;
    private Boolean currentlyWorking;
    private String description;
}