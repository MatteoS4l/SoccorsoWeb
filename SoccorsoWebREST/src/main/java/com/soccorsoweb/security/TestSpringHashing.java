package com.soccorsoweb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSpringHashing {

    public static void main(String[] args) {
        String passwordInChiaro = "password";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(passwordInChiaro);
        System.out.println("Hash della password: " + hash);
    }
}

// classe per prendere la password criptata e metterla nel db direttamente