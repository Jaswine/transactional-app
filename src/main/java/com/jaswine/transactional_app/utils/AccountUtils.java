package com.jaswine.transactional_app.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@UtilityClass
public class AccountUtils {

    public String generateAddress() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = Instant.now().toString() + Math.random();
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return "acc_" + bytesToHex(hash).substring(0, 16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating address", e);
        }
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
