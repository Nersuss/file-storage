package ru.nersus.storage.dto;

import java.util.UUID;

public record AuthWithSessionRsDto(
        String username,
        UUID session
) {
}
