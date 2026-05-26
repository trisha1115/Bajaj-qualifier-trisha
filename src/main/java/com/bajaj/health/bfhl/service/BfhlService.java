package com.bajaj.health.bfhl.service;

import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;

public interface BfhlService {
    BfhlResponse processRequest(BfhlRequest request);
}