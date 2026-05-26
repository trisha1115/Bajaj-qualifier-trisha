package com.bajaj.health.bfhl.service.impl;

import com.bajaj.health.bfhl.config.BfhlConfig;
import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;
import com.bajaj.health.bfhl.exception.BfhlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Service-level unit tests validating core classifications, calculation algorithms, and edge-case behaviors.
 */
public class BfhlServiceImplTest {

    private BfhlConfig bfhlConfig;
    private BfhlServiceImpl bfhlService;

    @BeforeEach
    public void setUp() {
        bfhlConfig = new BfhlConfig("john_doe", "17091999", "john@xyz.com", "ABCD123");
        bfhlService = new BfhlServiceImpl(bfhlConfig);
    }

    @Test
    public void testSuccessfulProcessingWithStandardPayload() {
        // Input matching the prompt assignment
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = bfhlService.processRequest(request);

        // Core assertions
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("john_doe_17091999", response.getUserId());
        assertEquals("john@xyz.com", response.getEmail());
        assertEquals("ABCD123", response.getRollNumber());

        // Numeric checks
        assertEquals(Collections.singletonList("1"), response.getOddNumbers());
        assertEquals(Arrays.asList("334", "4"), response.getEvenNumbers());
        assertEquals("339", response.getSum());

        // Alphabetic checks
        assertEquals(Arrays.asList("A", "R"), response.getAlphabets());

        // Special characters check
        assertEquals(Collections.singletonList("$"), response.getSpecialCharacters());

        // Alternating reversal check
        // Collected "a", "R" -> reversed "Ra" -> alternate caps -> "Ra"
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    public void testEmptyPayloadDoesNotFail() {
        BfhlRequest request = new BfhlRequest(Collections.emptyList());
        BfhlResponse response = bfhlService.processRequest(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    public void testNullPayloadThrowsValidationException() {
        assertThrows(BfhlException.class, () -> {
            bfhlService.processRequest(null);
        });
    }

    @Test
    public void testNullDataListThrowsValidationException() {
        BfhlRequest request = new BfhlRequest(null);
        assertThrows(BfhlException.class, () -> {
            bfhlService.processRequest(request);
        });
    }

    @Test
    public void testLargeNumbersDoNotOverflowSum() {
        // Verify BigInteger functionality with high numeric strings
        BfhlRequest request = new BfhlRequest(Arrays.asList("999999999999999999", "1"));
        BfhlResponse response = bfhlService.processRequest(request);

        assertEquals("1000000000000000000", response.getSum());
    }

    @Test
    public void testComplexConcatStringReversalAndCaps() {
        // Check "A" + "ABCD" + "DOE" -> collected letters: "AABCDDOE"
        // Reversed: "EODDCBAA"
        // Alternating: "EoDdCbAa"
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse response = bfhlService.processRequest(request);

        assertEquals("EoDdCbAa", response.getConcatString());
    }
}
