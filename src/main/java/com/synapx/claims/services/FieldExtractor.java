package com.synapx.claims.services;

import com.synapx.claims.dtos.ExtractedFields;
import com.synapx.claims.dtos.ProcessClaimResult;
import com.synapx.claims.util.TextUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldExtractor {

    public ProcessClaimResult extract(String rawText) {

        String text = TextUtil.clean(rawText);

        ExtractedFields fields = new ExtractedFields();

        // ---------------- POLICY INFO ----------------
        fields.setPolicyNumber(TextUtil.find(text,
                "POLICY NUMBER\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setPolicyholderName(TextUtil.find(text,
                "POLICYHOLDER NAME\\s*[:\\-]?\\s*([^\\n]+)",
                "NAME OF INSURED\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setEffectiveDates(TextUtil.find(text,
                "EFFECTIVE DATES\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        // ---------------- INCIDENT INFO ----------------
        fields.setIncidentDate(TextUtil.find(text,
                "\\bDATE\\b\\s*[:\\-]?\\s*([^\\n]+)",
                "DATE OF LOSS\\s*[:\\-]?\\s*([^\\n]+)"
        ));


        fields.setIncidentTime(TextUtil.find(text,
                "TIME\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setIncidentLocation(TextUtil.find(text,
                "LOCATION\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setIncidentDescription(TextUtil.find(text,
                "DESCRIPTION\\s*[:\\-]?\\s*([^\\n]+)",
                "DESCRIPTION OF ACCIDENT\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        // ---------------- INVOLVED PARTIES ----------------
        fields.setClaimant(TextUtil.find(text,
                "CLAIMANT\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setContactDetails(TextUtil.find(text,
                "CONTACT DETAILS\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setThirdParties(TextUtil.find(text,
                "THIRD PARTIES\\s*[:\\-]?\\s*([^\\n]+)",
                "THIRD PARTY\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        // ---------------- ASSET DETAILS ----------------
        fields.setAssetType(TextUtil.find(text,
                "ASSET TYPE\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        fields.setAssetId(TextUtil.find(text,
                "ASSET ID\\s*[:\\-]?\\s*([^\\n]+)",
                "VIN\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        String estDamage = TextUtil.find(text,
                "ESTIMATED DAMAGE\\s*[:\\-]?\\s*\\$?([0-9,]+(\\.[0-9]{1,2})?)"
        );
        fields.setEstimatedDamage(TextUtil.toDouble(estDamage));

        // ---------------- MANDATORY FIELDS ----------------
        fields.setClaimType(TextUtil.find(text,
                "CLAIM TYPE\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        String initialEstimate = TextUtil.find(text,
                "INITIAL ESTIMATE\\s*[:\\-]?\\s*\\$?([0-9,]+(\\.[0-9]{1,2})?)"
        );
        fields.setInitialEstimate(TextUtil.toDouble(initialEstimate));

        fields.setAttachments(TextUtil.find(text,
                "ATTACHMENTS\\s*[:\\-]?\\s*([^\\n]+)"
        ));

        // ---------------- MISSING FIELDS CHECK ----------------
        List<String> missing = new ArrayList<>();

        // Policy
        checkMissing(fields.getPolicyNumber(), "policyNumber", missing);
        checkMissing(fields.getPolicyholderName(), "policyholderName", missing);
        checkMissing(fields.getEffectiveDates(), "effectiveDates", missing);

        // Incident
        checkMissing(fields.getIncidentDate(), "incidentDate", missing);
        checkMissing(fields.getIncidentTime(), "incidentTime", missing);
        checkMissing(fields.getIncidentLocation(), "incidentLocation", missing);
        checkMissing(fields.getIncidentDescription(), "incidentDescription", missing);

        // Parties
        checkMissing(fields.getClaimant(), "claimant", missing);
        checkMissing(fields.getContactDetails(), "contactDetails", missing);

        // Asset
        checkMissing(fields.getAssetType(), "assetType", missing);
        checkMissing(fields.getAssetId(), "assetId", missing);
        if (fields.getEstimatedDamage() == null) missing.add("estimatedDamage");

        // Mandatory
        checkMissing(fields.getClaimType(), "claimType", missing);
        checkMissing(fields.getAttachments(), "attachments", missing);
        if (fields.getInitialEstimate() == null) missing.add("initialEstimate");

        return new ProcessClaimResult(fields, missing);
    }

    private void checkMissing(String value, String fieldName, List<String> missing) {
        if (value == null || value.trim().isEmpty()) {
            missing.add(fieldName);
        }
    }
}
