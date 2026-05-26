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

@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @PostMapping
    public ResponseEntity<BfhlResponse> handleBfhlRequest(@RequestBody BfhlRequest request) {
        log.info("Received POST /bfhl");
        BfhlResponse response = bfhlService.processRequest(request);
        return ResponseEntity.ok(response);
    }

    // GET just returns operation_code: 1 as per the challenge spec
    @GetMapping
    public ResponseEntity<Map<String, Object>> handleBfhlGetRequest() {
        Map<String, Object> result = new HashMap<>();
        result.put("operation_code", 1);
        return ResponseEntity.ok(result);
    }
}