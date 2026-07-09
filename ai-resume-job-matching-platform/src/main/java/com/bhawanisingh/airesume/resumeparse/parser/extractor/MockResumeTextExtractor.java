package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import org.springframework.stereotype.Component;

@Component
public class MockResumeTextExtractor implements ResumeTextExtractor {

    @Override
    public String extractText(Resume resume) {
        if (resume == null) {
            throw new ResumeParsingException("Resume is required for text extraction");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Mock extracted text for resume file: ")
                .append(resume.getOriginalFileName());

        if (resume.getFileType() != null) {
            sb.append(" | fileType=").append(resume.getFileType());
        }

        if (resume.getFileSize() != null) {
            sb.append(" | fileSize=").append(resume.getFileSize());
        }

        return sb.toString();
    }
}