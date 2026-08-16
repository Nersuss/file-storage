package ru.nersus.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nersus.storage.dto.AuthRsDto;
import ru.nersus.storage.entity.User;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Rest-controller for user")
public class UserController {

    @GetMapping("/api/user/me")
    @Operation(summary = "Getting current user details")
    AuthRsDto getUser(@AuthenticationPrincipal User user) {

        return new AuthRsDto(user.getUsername());
    }

}
