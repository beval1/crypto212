package com.crypto212.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
