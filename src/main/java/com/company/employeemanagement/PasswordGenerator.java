package com.company.employeemanagement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        String password = "Admin123";

        String hashedPassword =
                new BCryptPasswordEncoder().encode(password);

        System.out.println(hashedPassword);
    }
}