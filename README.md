# Autonomous Insurance Claims Processing Agent (Spring Boot)

## Overview
This project is a lightweight autonomous agent that processes FNOL (First Notice of Loss) documents and returns structured claim data.

It supports:
- FNOL documents in **PDF** and **TXT** formats
- Field extraction (policy, incident, parties, asset)
- Missing field detection
- Rule-based claim routing
- JSON output in the required assessment format

---

## Features

### 1. Field Extraction
Extracts:
- Policy Number, Policyholder Name, Effective Dates
- Incident Date, Time, Location, Description
- Claimant, Third Parties, Contact Details
- Asset Type, Asset ID, Estimated Damage
- Claim Type, Attachments, Initial Estimate

### 2. Missing Field Detection
Detects missing mandatory fields such as:
- policyNumber
- incidentDate
- incidentLocation
- estimatedDamage
- claimType
- initialEstimate

### 3. Routing Rules
- If estimated damage < 25,000 → **Fast-track**
- If any mandatory field is missing → **Manual review**
- If description contains “fraud”, “inconsistent”, “staged” → **Investigation Flag**
- If claim type = injury → **Specialist Queue**

---

## Tech Stack
- Java 17
- Spring Boot 3
- Apache PDFBox (PDF text extraction)
- Swagger / OpenAPI
- JUnit 5

---
## Sample Test Files
Sample FNOL documents for testing are available in:
`src/test/resources/samples/`

---

## Live Demo (Render)
Swagger UI: https://claims-processing-agent.onrender.com/swagger-ui/index.html  
Health Check: https://claims-processing-agent.onrender.com/api/claims/health
---

## API Endpoints

### Health Check
`GET /api/claims/health`

### Process FNOL Document
`POST /api/claims/process`

Consumes:
- multipart/form-data
- file (PDF/TXT)



Response JSON format:
```json
{
  "extractedFields": {},
  "missingFields": [],
  "recommendedRoute": "",
  "reasoning": ""
}








