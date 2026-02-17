package com.grctool.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OCRService {

    private final PolicyRepository policyRepository;

    @Async("taskExecutor")
    @Transactional
    public void extractTextFromPdf(UUID policyId, String filePath) {
        log.info("Starting hybrid extraction for policy: {}", policyId);
        File pdfFile = new File(filePath);
        String extractedText = "";

        // --- STEP 1: Attempt Digital Text Extraction (Fast) ---
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document).trim();
            
            if (!extractedText.isEmpty()) {
                log.info("Digital text extracted successfully for policy: {}", policyId);
                finishProcessing(policyId, extractedText);
                return; // Skip OCR entirely!
            }
        } catch (IOException e) {
            log.warn("Digital extraction failed for {}, trying OCR fallback...", policyId);
        }

        // --- STEP 2: Fallback to OCR (Heavy) ---
        log.info("Document appears to be a scan. Starting OCR for: {}", policyId);
        Tesseract tesseract = new Tesseract();
        // Update this path to your local Tesseract install (Windows or Ubuntu)
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(1); // Use LSTM for better performance

        try {
            extractedText = tesseract.doOCR(pdfFile);
            log.info("OCR completed for policy: {}", policyId);
            finishProcessing(policyId, extractedText);
        } catch (TesseractException e) {
            log.error("OCR Failed for policy: {}. Error: {}", policyId, e.getMessage());
            finishProcessing(policyId, "ERROR: Could not extract text. Document may be a low-quality scan.");
        }
    }

    private void finishProcessing(UUID policyId, String content) {
        policyRepository.findById(policyId).ifPresent(policy -> {
            policy.setContent(content);
            policy.setProcessed(true); // Signal the UI to stop the spinner
            policyRepository.save(policy);
            log.debug("Database updated for policy: {}", policyId);
        });
    }
}