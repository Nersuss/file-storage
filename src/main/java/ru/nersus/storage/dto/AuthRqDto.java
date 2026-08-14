package ru.nersus.storage.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

public record AuthRqDto(
        String username,
        String password
) {
    public AuthRqDto encodePassword(PasswordEncoder passwordEncoder) {
        return new AuthRqDto(username, passwordEncoder.encode(password));
    }
}
