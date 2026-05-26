package com.bajaj.health.bfhl.controller;

import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;
import com.bajaj.health.bfhl.service.BfhlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class exposing the REST endpoints for the BFHL api under the /bfhl context.
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*") // CrossOrigin enables easy integration with any frontend UI (React, Angular, Vue, etc.)
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);
    
    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * Handles POST requests for classifications and calculations.
     *
     * @param request the input payload containing the 'data' array
     * @return the processed response details with HTTP status 200 OK
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> handleBfhlRequest(@RequestBody BfhlRequest request) {
        log.info("POST request received at /bfhl endpoint, passing to service layer");
        BfhlResponse response = bfhlService.processRequest(request);
        log.info("Service processing finished. Dispatching payload response back to client");
        return ResponseEntity.ok(response);
    }

    /**
     * Fallback GET request handler matching common Bajaj developer challenge patterns.
     * Exposes the system operation status.
     *
     * @return operation details mapping with HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> handleBfhlGetRequest() {
        log.info("GET request received at /bfhl, generating static operation code response");
        Map<String, Object> operationResult = new HashMap<>();
        operationResult.put("operation_code", 1);
        return ResponseEntity.ok(operationResult);
    }
}
