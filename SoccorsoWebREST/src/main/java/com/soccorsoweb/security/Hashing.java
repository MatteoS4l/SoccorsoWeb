package com.soccorsoweb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// classe per l'hash

public class Hashing {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password_chiara) {
        return encoder.encode(password_chiara);
    }

    public static boolean checkPassword(String password_chiara, String password_hashed) {
        return encoder.matches(password_chiara, password_hashed);
    }
}