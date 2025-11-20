package com.example.eventmanager.util;
import java.security.SecureRandom;
import java.util.Base64;

public class Generator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String securityToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String otp() {
        return String.format("%06d", 100000 + secureRandom.nextInt(900000));
    }

    public static String ticketNumber() {
        return String.format("T%07d", 1000000 + secureRandom.nextInt(9000000));
    }
}
