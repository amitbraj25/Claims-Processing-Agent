package com.synapx.claims.services;


import com.synapx.claims.dtos.ClaimResponse;
import com.synapx.claims.dtos.ExtractedFields;
import com.synapx.claims.dtos.ProcessClaimResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimProcessingService {

    private final DocumentTextExtractor documentTextExtractor;
    private final FieldExtractor fieldExtractor;
    private final RoutingEngine routingEngine;

    public ClaimResponse process(MultipartFile file) {

        String text = documentTextExtractor.extractText(file);

        ProcessClaimResult extracted = fieldExtractor.extract(text);

        ExtractedFields fields = extracted.getExtractedFields();
        List<String> missing = extracted.getMissingFields();

        String route = routingEngine.route(fields, missing);
        String reasoning = routingEngine.buildReasoning(fields, missing, route);

        return new ClaimResponse(fields, missing, route, reasoning);
    }
}

