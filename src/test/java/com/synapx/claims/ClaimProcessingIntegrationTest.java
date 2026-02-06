package com.synapx.claims;

import com.synapx.claims.dtos.ClaimResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ClaimProcessingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<ClaimResponse> uploadSample(String filename) throws Exception {

        Path path = Path.of("src/test/resources/samples/" + filename);
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(
                "file",
                filename,
                "text/plain",
                content
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Build multipart request
        org.springframework.util.MultiValueMap<String, Object> body =
                new org.springframework.util.LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<org.springframework.util.MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        return restTemplate.postForEntity("/api/claims/process", requestEntity, ClaimResponse.class);
    }

    @Test
    void testFastTrack() throws Exception {
        ResponseEntity<ClaimResponse> response = uploadSample("fnol_fasttrack.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Fast-track", response.getBody().getRecommendedRoute());
        assertTrue(response.getBody().getMissingFields().isEmpty());
    }

    @Test
    void testManualReviewMissingFields() throws Exception {
        ResponseEntity<ClaimResponse> response = uploadSample("fnol_manualreview_missing.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Manual review", response.getBody().getRecommendedRoute());

        assertTrue(response.getBody().getMissingFields().contains("policyNumber"));
        assertTrue(response.getBody().getMissingFields().contains("estimatedDamage"));
        assertTrue(response.getBody().getMissingFields().contains("initialEstimate"));
    }

    @Test
    void testInvestigationFlag() throws Exception {
        ResponseEntity<ClaimResponse> response = uploadSample("fnol_investigation.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Investigation Flag", response.getBody().getRecommendedRoute());
        assertTrue(response.getBody().getMissingFields().isEmpty());
    }

    @Test
    void testSpecialistQueueInjury() throws Exception {
        ResponseEntity<ClaimResponse> response = uploadSample("fnol_injury.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Specialist Queue", response.getBody().getRecommendedRoute());
        assertTrue(response.getBody().getMissingFields().isEmpty());
    }
}
