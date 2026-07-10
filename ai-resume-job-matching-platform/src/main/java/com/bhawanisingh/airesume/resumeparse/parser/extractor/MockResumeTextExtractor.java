package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MockResumeTextExtractor implements ResumeTextExtractor {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("pdf", "docx", "txt");

    private final ResumeFileResolver resumeFileResolver;
    private final PdfResumeTextExtractor pdfResumeTextExtractor;
    private final DocxResumeTextExtractor docxResumeTextExtractor;
    private final TextResumeTextExtractor textResumeTextExtractor;

    @Override
    public String extractText(Resume resume) {
        Path resumeFilePath = resumeFileResolver.resolveResumeFilePath(resume);
        String extension = resumeFileResolver.getExtension(resumeFilePath);

        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new ResumeParsingException(
                    "Unsupported resume file format: " + extension + ". Supported formats are: pdf, docx, txt"
            );
        }

        return switch (extension) {
            case "pdf" -> pdfResumeTextExtractor.extract(resumeFilePath);
            case "docx" -> docxResumeTextExtractor.extract(resumeFilePath);
            case "txt" -> textResumeTextExtractor.extract(resumeFilePath);
            default -> throw new ResumeParsingException("No extractor available for file extension: " + extension);
        };
    }
}