package ru.nersus.storage.dto;

public record ResourceRsDto(
        String path,
        String name,
        Long size,
        ResourceType type
) {
}
