package com.bajaj.health.bfhl.dto;

import java.util.List;

/**
 * Data Transfer Object representing the request payload for /bfhl endpoint.
 */
public class BfhlRequest {

    private List<String> data;

    public BfhlRequest() {
    }

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
