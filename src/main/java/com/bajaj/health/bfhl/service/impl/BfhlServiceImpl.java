package com.bajaj.health.bfhl.service.impl;

import com.bajaj.health.bfhl.config.BfhlConfig;
import com.bajaj.health.bfhl.dto.BfhlRequest;
import com.bajaj.health.bfhl.dto.BfhlResponse;
import com.bajaj.health.bfhl.exception.BfhlException;
import com.bajaj.health.bfhl.service.BfhlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Production-grade implementation of the BfhlService interface.
 * Classifies elements, sums numeric values, reverses and alternates caps of alphabetical letters.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Logger log = LoggerFactory.getLogger(BfhlServiceImpl.class);

    private final BfhlConfig bfhlConfig;

    public BfhlServiceImpl(BfhlConfig bfhlConfig) {
        this.bfhlConfig = bfhlConfig;
    }

    @Override
    public BfhlResponse processRequest(BfhlRequest request) {
        if (request == null || request.getData() == null) {
            log.warn("Invalid payload received: request or data field is null");
            throw new BfhlException("Request data payload cannot be null");
        }

        log.info("Processing payload with {} input elements", request.getData().size());

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        BigInteger sumVal = BigInteger.ZERO;
        StringBuilder lettersCollector = new StringBuilder();

        for (String item : request.getData()) {
            if (item == null) {
                continue;
            }

            // Trim leading/trailing spaces for clean matching
            String trimmedItem = item.trim();

            if (trimmedItem.matches("^-?\\d+$")) {
                // Numeric item detected
                // BigInteger check for large numbers to prevent overflow
                BigInteger num = new BigInteger(trimmedItem);
                sumVal = sumVal.add(num);

                // Use the absolute value's modulo for even/odd check
                if (num.abs().mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                    evenNumbers.add(trimmedItem);
                } else {
                    oddNumbers.add(trimmedItem);
                }
            } else if (trimmedItem.matches("^[a-zA-Z]+$")) {
                // Alphabet-only string detected
                alphabets.add(trimmedItem.toUpperCase());
            } else {
                // Special characters or mixed format elements
                specialCharacters.add(trimmedItem);
            }

            // Extract all individual alphabetic letters for the concatenation logic
            for (char ch : trimmedItem.toCharArray()) {
                if (Character.isLetter(ch)) {
                    lettersCollector.append(ch);
                }
            }
        }

        // Apply concat_string logic: reverse the collected letters
        String reversedLetters = lettersCollector.reverse().toString();

        // Apply alternating caps: 1st uppercase, 2nd lowercase, 3rd uppercase, 4th lowercase
        StringBuilder alternatingCapsBuilder = new StringBuilder();
        for (int i = 0; i < reversedLetters.length(); i++) {
            char ch = reversedLetters.charAt(i);
            if (i % 2 == 0) {
                alternatingCapsBuilder.append(Character.toUpperCase(ch));
            } else {
                alternatingCapsBuilder.append(Character.toLowerCase(ch));
            }
        }
        String concatString = alternatingCapsBuilder.toString();

        log.info("Successfully processed elements. Even: {}, Odd: {}, Alphabets: {}, Specials: {}", 
                evenNumbers.size(), oddNumbers.size(), alphabets.size(), specialCharacters.size());

        log.debug("Computed sum: {}, generated alternating cap sequence: {}", sumVal, concatString);

        return new BfhlResponse(
                true,
                bfhlConfig.getUserId(),
                bfhlConfig.getEmail(),
                bfhlConfig.getRollNumber(),
                oddNumbers,
                evenNumbers,
                alphabets,
                specialCharacters,
                sumVal.toString(),
                concatString
        );
    }
}
