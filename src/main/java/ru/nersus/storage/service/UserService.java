package ru.nersus.storage.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.dto.AuthWithSessionRsDto;
import ru.nersus.storage.entity.Session;
import ru.nersus.storage.entity.User;
import ru.nersus.storage.exception.UserAlreadyExistsException;
import ru.nersus.storage.mapper.AuthRqDtoMapper;
import ru.nersus.storage.repo.SessionRepo;
import ru.nersus.storage.repo.UserRepo;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepo userRepo;
    AuthRqDtoMapper authRqDtoMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    SessionRepo sessionRepo;

    public AuthWithSessionRsDto signUp(AuthRqDto authRqDto) {
        if (userRepo.existsByEmail(authRqDto.username())) {
            log.info("User with email: {} already exists", authRqDto.username());
            throw new UserAlreadyExistsException(String.format("User with email: %s already exists", authRqDto.username()));
        }
        AuthRqDto encryptUser = authRqDto.encodePassword(bCryptPasswordEncoder);
        User saveUser = userRepo.save(authRqDtoMapper.toEntity(encryptUser));

        Session session = new Session(UUID.randomUUID().toString(), saveUser.getId(), saveUser.getEmail());
        sessionRepo.save(session);

        return new AuthWithSessionRsDto(saveUser.getEmail(), UUID.fromString(session.getId()));
    }

}
