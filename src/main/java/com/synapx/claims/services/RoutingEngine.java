package com.synapx.claims.services;

import com.synapx.claims.dtos.ExtractedFields;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutingEngine {

    public String route(ExtractedFields fields, List<String> missingFields) {

        // Rule 1: Investigation keywords
        String desc = fields.getIncidentDescription();
        if (desc != null) {
            String lower = desc.toLowerCase();
            if (lower.contains("fraud") || lower.contains("inconsistent") || lower.contains("staged")) {
                return "Investigation Flag";
            }
        }

        // Rule 2: Missing mandatory fields
        if (missingFields != null && !missingFields.isEmpty()) {
            return "Manual review";
        }

        // Rule 3: Claim type = injury
        if (fields.getClaimType() != null && fields.getClaimType().equalsIgnoreCase("injury")) {
            return "Specialist Queue";
        }

        // Rule 4: Damage < 25000
        if (fields.getEstimatedDamage() != null && fields.getEstimatedDamage() < 25000) {
            return "Fast-track";
        }

        // Default
        return "Standard Processing";
    }

    public String buildReasoning(ExtractedFields fields, List<String> missingFields, String route) {

        if ("Investigation Flag".equalsIgnoreCase(route)) {
            return "Claim description contains fraud-related keywords (fraud/inconsistent/staged).";
        }

        if ("Manual review".equalsIgnoreCase(route)) {
            return "Mandatory fields are missing: " + missingFields;
        }

        if ("Specialist Queue".equalsIgnoreCase(route)) {
            return "Claim type is injury and requires specialist handling.";
        }

        if ("Fast-track".equalsIgnoreCase(route)) {
            return "Estimated damage is below 25000 and no mandatory fields are missing.";
        }

        return "Claim does not meet fast-track, investigation, injury, or missing-field conditions.";
    }
}
