package ru.nersus.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.dto.AuthRsDto;
import ru.nersus.storage.dto.AuthWithSessionRsDto;
import ru.nersus.storage.service.AuthService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Rest-controller for authentication")
@RequestMapping("/api/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "Registration")
    @ResponseStatus(HttpStatus.CREATED)
    AuthRsDto registration(@RequestBody AuthRqDto authRqDto, HttpServletResponse response) {
        AuthWithSessionRsDto authWithSessionRsDto = authService.signUp(authRqDto);
        Cookie cookie = new Cookie("session", authWithSessionRsDto.session().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new AuthRsDto(authWithSessionRsDto.username());
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Authentication")
    AuthRsDto authentication(@RequestBody AuthRqDto authRqDto, HttpServletResponse response) {
        AuthWithSessionRsDto authWithSessionRsDto = authService.signIn(authRqDto);
        Cookie cookie = new Cookie("session", authWithSessionRsDto.session().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new AuthRsDto(authWithSessionRsDto.username());
    }

    @PostMapping("/sign-out")
    @Operation(summary = "Logout")
    void logout() {
    }

}
