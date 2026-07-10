package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TextResumeTextExtractor {

    public String extract(Path filePath) {
        try {
            String extractedText = Files.readString(filePath, StandardCharsets.UTF_8);

            if (!StringUtils.hasText(extractedText)) {
                throw new ResumeParsingException("No readable text found inside text resume: " + filePath.getFileName());
            }

            return extractedText.trim();
        } catch (IOException exception) {
            throw new ResumeParsingException(
                    "Failed to extract text from text resume: " + filePath.getFileName(),
                    exception
            );
        }
    }
}