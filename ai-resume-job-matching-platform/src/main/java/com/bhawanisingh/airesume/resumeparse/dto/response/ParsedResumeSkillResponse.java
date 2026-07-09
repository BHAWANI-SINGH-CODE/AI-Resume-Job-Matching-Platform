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
public class ParsedResumeSkillResponse {

    private Long id;
    private String skillName;
    private String normalizedSkillName;
}