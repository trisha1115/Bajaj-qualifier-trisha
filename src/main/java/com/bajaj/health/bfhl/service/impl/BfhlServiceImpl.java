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
import java.util.List;

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
            throw new BfhlException("data cannot be null");
        }

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        BigInteger sum = BigInteger.ZERO;
        StringBuilder letters = new StringBuilder();

        for (String item : request.getData()) {
            if (item == null)
                continue;

            String s = item.trim();

            if (s.matches("^-?\\d+$")) {
                BigInteger num = new BigInteger(s);
                sum = sum.add(num);
                if (num.abs().mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                    evenNumbers.add(s);
                } else {
                    oddNumbers.add(s);
                }
            } else if (s.matches("^[a-zA-Z]+$")) {
                alphabets.add(s.toUpperCase());
            } else {
                specialCharacters.add(s);
            }

            for (char ch : s.toCharArray()) {
                if (Character.isLetter(ch))
                    letters.append(ch);
            }
        }

        String reversed = letters.reverse().toString();
        StringBuilder concatBuilder = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char ch = reversed.charAt(i);
            concatBuilder.append(i % 2 == 0 ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        }

        log.info("done - odd:{} even:{} alpha:{} special:{}", oddNumbers.size(), evenNumbers.size(), alphabets.size(),
                specialCharacters.size());

        return new BfhlResponse(
                true,
                bfhlConfig.getUserId(),
                bfhlConfig.getEmail(),
                bfhlConfig.getRollNumber(),
                oddNumbers,
                evenNumbers,
                alphabets,
                specialCharacters,
                sum.toString(),
                concatBuilder.toString());
    }
}