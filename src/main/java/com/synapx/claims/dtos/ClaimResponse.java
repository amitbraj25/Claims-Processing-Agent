package com.synapx.claims.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {

    private ExtractedFields extractedFields;
    private List<String> missingFields;
    private String recommendedRoute;
    private String reasoning;
}

