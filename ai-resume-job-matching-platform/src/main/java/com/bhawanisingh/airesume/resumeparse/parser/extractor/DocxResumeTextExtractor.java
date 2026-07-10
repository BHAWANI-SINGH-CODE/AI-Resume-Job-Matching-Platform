package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocxResumeTextExtractor {

    public String extract(Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath);
             XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            String extractedText = extractor.getText();

            if (!StringUtils.hasText(extractedText)) {
                throw new ResumeParsingException("No readable text found inside DOCX resume: " + filePath.getFileName());
            }

            return extractedText.trim();
        } catch (IOException exception) {
            throw new ResumeParsingException(
                    "Failed to extract text from DOCX resume: " + filePath.getFileName(),
                    exception
            );
        }
    }
}