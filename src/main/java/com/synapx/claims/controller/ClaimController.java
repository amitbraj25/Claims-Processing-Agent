package com.synapx.claims.controller;

import com.synapx.claims.dtos.ClaimResponse;
import com.synapx.claims.services.ClaimProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimProcessingService claimProcessingService;

    @Operation(
            summary = "Process FNOL Document",
            description = "Upload a PDF or TXT FNOL document. The system extracts key fields, detects missing fields, and routes the claim."
    )
    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ClaimResponse processClaim(
            @Parameter(description = "FNOL document file (PDF or TXT)", required = true)
            @RequestParam("file") MultipartFile file
    ) {
        return claimProcessingService.process(file);
    }

    @Operation(summary = "Health Check")
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
