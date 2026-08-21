package ru.nersus.storage.dto;

import java.util.List;

public record UploadRsDto(
        List<ResourceRsDto> uploads
) {
}
