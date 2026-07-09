package com.bhawanisingh.airesume.resumeparse.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ResumeParsingTriggerResponse;
import com.bhawanisingh.airesume.resumeparse.service.ResumeParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resume-parsing")
@RequiredArgsConstructor
public class ResumeParsingController {

    private final ResumeParsingService resumeParsingService;

    @PostMapping("/resumes/{resumeId}/parse")
    public ResponseEntity<ApiResponse<ResumeParsingTriggerResponse>> triggerResumeParsing(
            @PathVariable Long resumeId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        ResumeParsingTriggerResponse response =
                resumeParsingService.triggerResumeParsing(resumeId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Resume parsing triggered successfully", response)
        );
    }

    @GetMapping("/resumes/{resumeId}")
    public ResponseEntity<ApiResponse<ParsedResumeResponse>> getParsedResumeByResumeId(
            @PathVariable Long resumeId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        ParsedResumeResponse response =
                resumeParsingService.getParsedResumeByResumeId(resumeId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Parsed resume fetched successfully", response)
        );
    }
}