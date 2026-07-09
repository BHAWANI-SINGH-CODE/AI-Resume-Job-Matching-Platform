package com.bhawanisingh.airesume.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationRequest {

    private Long resumeId;

    @Size(max = 3000, message = "Cover letter must not exceed 3000 characters")
    private String coverLetter;
}