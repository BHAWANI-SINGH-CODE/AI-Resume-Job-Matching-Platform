package com.bhawanisingh.airesume.resumeparse.parser.extractor;

import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resumeparse.parser.exception.ResumeParsingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Component
public class ResumeFileResolver {

    public Path resolveResumeFilePath(Resume resume) {
        if (resume == null) {
            throw new ResumeParsingException("Resume cannot be null for file resolution.");
        }

        String resumeFilePath = resume.getFilePath();
        if (!StringUtils.hasText(resumeFilePath)) {
            throw new ResumeParsingException("Resume file path is missing for resume id: " + resume.getId());
        }

        Path path = Paths.get(resumeFilePath).normalize().toAbsolutePath();

        if (!Files.exists(path)) {
            throw new ResumeParsingException("Resume file does not exist at path: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new ResumeParsingException("Resolved resume path is not a file: " + path);
        }

        return path;
    }

    public String getExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase(Locale.ROOT);
    }
}