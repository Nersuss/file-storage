package ru.nersus.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nersus.storage.dto.AuthRsDto;
import ru.nersus.storage.entity.User;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Rest-controller for resources (files, catalogues)")
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping
    @Operation(summary = "Get resource details")
    AuthRsDto getResource(@AuthenticationPrincipal User user) {

        return new AuthRsDto(user.getUsername());
    }

    @PostMapping
    @Operation(summary = "Upload resource")
    AuthRsDto postResource(@AuthenticationPrincipal User user) {

        return new AuthRsDto(user.getUsername());
    }

}
