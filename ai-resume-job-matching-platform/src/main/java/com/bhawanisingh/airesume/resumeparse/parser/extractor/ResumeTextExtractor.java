package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resume.entity.Resume;

public interface ResumeTextExtractor {

    String extractText(Resume resume);
}