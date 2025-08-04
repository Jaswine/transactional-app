package com.jaswine.transactional_app.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@UtilityClass
public class TransactionUtils {

    public String generateTransactionSignature() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = Instant.now().toString() + Math.random();
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return AccountUtils.bytesToHex(hash).substring(0, 64);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating address", e);
        }
    }

}
