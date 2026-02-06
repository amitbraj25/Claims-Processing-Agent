package com.synapx.claims.dtos;

import lombok.Data;

@Data
public class ExtractedFields {

    // Policy Information
    private String policyNumber;
    private String policyholderName;
    private String effectiveDates;

    // Incident Information
    private String incidentDate;
    private String incidentTime;
    private String incidentLocation;
    private String incidentDescription;

    // Involved Parties
    private String claimant;
    private String thirdParties;
    private String contactDetails;

    // Asset Details
    private String assetType;
    private String assetId;
    private Double estimatedDamage;

    // Mandatory Fields
    private String claimType;
    private String attachments;
    private Double initialEstimate;
}

