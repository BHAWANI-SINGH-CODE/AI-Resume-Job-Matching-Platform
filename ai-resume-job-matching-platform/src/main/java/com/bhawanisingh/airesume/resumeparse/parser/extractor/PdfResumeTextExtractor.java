package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class PdfResumeTextExtractor {

    public String extract(Path filePath) {
        try (PDDocument document = Loader.loadPDF(filePath.toFile())) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String extractedText = textStripper.getText(document);

            if (!StringUtils.hasText(extractedText)) {
                throw new ResumeParsingException(
                        "No readable text found inside PDF resume: " + filePath.getFileName()
                );
            }

            return extractedText.trim();
        } catch (IOException exception) {
            throw new ResumeParsingException(
                    "Failed to extract text from PDF resume: " + filePath.getFileName(),
                    exception
            );
        }
    }
}