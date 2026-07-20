package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.ai.AiResumeParserEngine; // Import your new AI engine
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class ResumeParsingOrchestrator {

    private final ResumeParserEngine resumeParserEngine;

    public ResumeParsingOrchestrator(AiResumeParserEngine aiResumeParserEngine) {
        this.resumeParserEngine = aiResumeParserEngine;
    }

    public ResumeParseResult parseResume(Resume resume) {
        return resumeParserEngine.parseResume(resume);
    }
}