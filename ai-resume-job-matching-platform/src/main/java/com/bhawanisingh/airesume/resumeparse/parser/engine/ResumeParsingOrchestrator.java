package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;
import org.springframework.stereotype.Component;

@Component
public class ResumeParsingOrchestrator {

    private final ResumeParserEngine resumeParserEngine;

    public ResumeParsingOrchestrator(ResumeParserEngine resumeParserEngine) {
        this.resumeParserEngine = resumeParserEngine;
    }

    public ResumeParseResult parseResume(Resume resume) {
        return resumeParserEngine.parse(resume);
    }
}