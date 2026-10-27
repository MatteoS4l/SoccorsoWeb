package com.soccorsoweb.rest.security;

import com.soccorsoweb.model.Utente;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TokenManager {

    private static final Map<String, Utente> activeTokens = new ConcurrentHashMap<>();
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    //token random
    public static String generateToken(Utente utente) {
        byte[] randomBytes = new byte[32]; // 256 bit di entropia
        secureRandom.nextBytes(randomBytes);
        String token = "jwt-" + base64Encoder.encodeToString(randomBytes).replace("=", "");

        activeTokens.put(token, utente);
        return token;
    }


    public static boolean isValid(String token) {
        return activeTokens.containsKey(token);
    }

    public static Utente getUtente(String token) {
        return activeTokens.get(token);
    }

    public static void invalidateToken(String token) {
        activeTokens.remove(token);
    }
}
