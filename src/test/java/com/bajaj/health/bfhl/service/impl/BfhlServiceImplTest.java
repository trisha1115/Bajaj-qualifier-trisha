package com.bajaj.health.bfhl.service.impl;

import com.bajaj.health.bfhl.config.BfhlConfig;
import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;
import com.bajaj.health.bfhl.exception.BfhlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class BfhlServiceImplTest {

    private BfhlConfig bfhlConfig;
    private BfhlServiceImpl bfhlService;

    @BeforeEach
    void setUp() {
        bfhlConfig = new BfhlConfig("john_doe", "17091999", "john@xyz.com", "ABCD123");
        bfhlService = new BfhlServiceImpl(bfhlConfig);
    }

    @Test
    void basicPayload() {
        BfhlResponse res = bfhlService.processRequest(
                new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$")));

        assertTrue(res.isSuccess());
        assertEquals("john_doe_17091999", res.getUserId());
        assertEquals(Collections.singletonList("1"), res.getOddNumbers());
        assertEquals(Arrays.asList("334", "4"), res.getEvenNumbers());
        assertEquals("339", res.getSum());
        assertEquals(Arrays.asList("A", "R"), res.getAlphabets());
        assertEquals(Collections.singletonList("$"), res.getSpecialCharacters());
        assertEquals("Ra", res.getConcatString());
    }

    @Test
    void emptyList() {
        BfhlResponse res = bfhlService.processRequest(new BfhlRequest(Collections.emptyList()));

        assertTrue(res.isSuccess());
        assertTrue(res.getOddNumbers().isEmpty());
        assertTrue(res.getEvenNumbers().isEmpty());
        assertEquals("0", res.getSum());
        assertEquals("", res.getConcatString());
    }

    @Test
    void nullRequest_throws() {
        assertThrows(BfhlException.class, () -> bfhlService.processRequest(null));
    }

    @Test
    void nullData_throws() {
        assertThrows(BfhlException.class, () -> bfhlService.processRequest(new BfhlRequest(null)));
    }

    @Test
    void largeNumbers() {
        BfhlResponse res = bfhlService.processRequest(
                new BfhlRequest(Arrays.asList("999999999999999999", "1")));
        assertEquals("1000000000000000000", res.getSum());
    }

    @Test
    void concatStringAlternatingCaps() {
        // "A","ABCD","DOE" -> collected "AABCDDOE" -> reversed "EODDCBAA" -> "EoDdCbAa"
        BfhlResponse res = bfhlService.processRequest(
                new BfhlRequest(Arrays.asList("A", "ABCD", "DOE")));
        assertEquals("EoDdCbAa", res.getConcatString());
    }
}