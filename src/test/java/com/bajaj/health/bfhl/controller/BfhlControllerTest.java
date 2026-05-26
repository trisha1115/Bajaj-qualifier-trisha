package com.bajaj.health.bfhl.controller;

import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;
import com.bajaj.health.bfhl.exception.BfhlException;
import com.bajaj.health.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-level integration tests using MockMvc.
 * Validates REST mapping routes, payloads, content types, status resolutions, and validation response bodies.
 */
@WebMvcTest(BfhlController.class)
public class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BfhlService bfhlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPostBfhlEndpointSuccessResponse() throws Exception {
        // Prepare mock service response matching specifications
        BfhlResponse mockResponse = new BfhlResponse(
                true,
                "john_doe_17091999",
                "john@xyz.com",
                "ABCD123",
                Collections.singletonList("1"),
                Arrays.asList("334", "4"),
                Arrays.asList("A", "R"),
                Collections.singletonList("$"),
                "339",
                "Ra"
        );

        when(bfhlService.processRequest(any(BfhlRequest.class))).thenReturn(mockResponse);

        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("john_doe_17091999"))
                .andExpect(jsonPath("$.email").value("john@xyz.com"))
                .andExpect(jsonPath("$.roll_number").value("ABCD123"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("R"))
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    public void testGetBfhlEndpointSuccessResponse() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code").value(1));
    }

    @Test
    public void testPostBfhlEndpointThrowsExceptionGracefulHandling() throws Exception {
        when(bfhlService.processRequest(any())).thenThrow(new BfhlException("Invalid empty data list provided"));

        BfhlRequest request = new BfhlRequest(null);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }
}
