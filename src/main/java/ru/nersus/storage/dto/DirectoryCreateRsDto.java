package ru.nersus.storage.dto;

public record DirectoryCreateRsDto(
        String path,
        String name,
        ResourceType type
) {
}
