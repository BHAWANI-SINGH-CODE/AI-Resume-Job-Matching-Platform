package com.bhawanisingh.airesume.resumeparse.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedSkillData {

    private String skillName;

    private String normalizedSkillName;

}