package com.bhawanisingh.airesume.resumeparse.dto.response;

import com.bhawanisingh.airesume.resumeparse.enums.ParsingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeParsingTriggerResponse {

    private Long parsedResumeId;
    private Long resumeId;
    private ParsingStatus parsingStatus;
    private String message;
    private LocalDateTime parsedAt;
}