package com.bhawanisingh.airesume.resumeparse.parser.engine;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.model.ResumeParseResult;

public interface ResumeParserEngine {

    ResumeParseResult parse(Resume resume);
}