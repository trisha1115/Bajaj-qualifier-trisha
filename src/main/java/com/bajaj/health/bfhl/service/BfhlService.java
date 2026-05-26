package com.bajaj.health.bfhl.service;

import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;

/**
 * Service interface defining the core business logic for processing BFHL payloads.
 */
public interface BfhlService {

    /**
     * Processes the incoming Request payload and extracts numbers, odd/even classification,
     * alphabets, special characters, sums numeric elements, and applies specific text reversal rules.
     *
     * @param request the incoming user payload request
     * @return the processed response containing extracted lists and calculated values
     */
    BfhlResponse processRequest(BfhlRequest request);
}
