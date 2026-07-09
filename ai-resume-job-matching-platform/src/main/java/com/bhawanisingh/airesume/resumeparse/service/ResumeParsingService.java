package com.bhawanisingh.airesume.resumeparse.service;

import com.bhawanisingh.airesume.resumeparse.dto.response.ParsedResumeResponse;
import com.bhawanisingh.airesume.resumeparse.dto.response.ResumeParsingTriggerResponse;

public interface ResumeParsingService {

    ResumeParsingTriggerResponse triggerResumeParsing(Long resumeId, String userEmail);

    ParsedResumeResponse getParsedResumeByResumeId(Long resumeId, String userEmail);
}