package com.synapx.claims.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentTextExtractor {

    public String extractText(MultipartFile file) {

        String filename = file.getOriginalFilename();
        if (filename == null) filename = "";

        try {

            if (filename.toLowerCase().endsWith(".pdf")) {
                return extractFromPdf(file);
            }

            if (filename.toLowerCase().endsWith(".txt")) {
                return extractFromTxt(file);
            }

            // fallback: treat as text
            return new String(file.getBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract document text: " + e.getMessage());
        }
    }

    private String extractFromPdf(MultipartFile file) throws Exception {

        try (InputStream is = file.getInputStream();
             PDDocument document = PDDocument.load(is)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromTxt(MultipartFile file) throws Exception {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}

