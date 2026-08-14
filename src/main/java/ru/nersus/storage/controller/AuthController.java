package ru.nersus.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.dto.AuthRsDto;
import ru.nersus.storage.service.AuthService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Rest-controller for authentication")
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "Registration")
    @ResponseStatus(HttpStatus.CREATED)
    AuthRsDto registration(@RequestBody AuthRqDto authRqDto) {
        return authService.signUp(authRqDto);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Authentication")
    AuthRsDto authentication(@RequestBody AuthRqDto authRqDto) {
        return authService.signIn(authRqDto);
    }

    @PostMapping("/sign-out")
    @Operation(summary = "Logout")
    void logout() {
    }

}
