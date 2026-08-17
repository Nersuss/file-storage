package ru.nersus.storage.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nersus.storage.entity.Session;
import ru.nersus.storage.entity.User;
import ru.nersus.storage.repo.SessionRepo;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionFilter extends OncePerRequestFilter {

    SessionRepo sessionRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!cookie.getName().equals("session")) {
                    continue;
                }
                Optional<Session> sessionEntity = sessionRepo.findById(cookie.getValue());
                if (sessionEntity.isEmpty()) {
                    log.warn("User session not found in redis");
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(new User(sessionEntity.get().getUserId(), sessionEntity.get().getEmail(), null));
            }
        }

        filterChain.doFilter(request, response);
    }
}
