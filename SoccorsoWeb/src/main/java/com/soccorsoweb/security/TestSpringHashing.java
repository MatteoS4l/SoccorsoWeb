package com.soccorsoweb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 

public class TestSpringHashing {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordInChiaro = "password";
        String hashedPassword = encoder.encode(passwordInChiaro);
        System.out.println("Password criptata di " + "'" + passwordInChiaro + "' è: " + hashedPassword);
    }
}
 
//mi serve per inserire le password direttamente nel db