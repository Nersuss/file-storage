package ru.nersus.storage.service;

import io.minio.MinioClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.dto.AuthWithSessionRsDto;
import ru.nersus.storage.entity.Session;
import ru.nersus.storage.entity.User;
import ru.nersus.storage.exception.UserAlreadyExistsException;
import ru.nersus.storage.mapper.AuthRqDtoMapper;
import ru.nersus.storage.repo.SessionRepo;
import ru.nersus.storage.repo.UserRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {

    UserRepo userRepo;
    AuthRqDtoMapper authRqDtoMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    SessionRepo sessionRepo;
    MinioClient minio;

    @Transactional
    public AuthWithSessionRsDto signUp(AuthRqDto authRqDto) {
        if (userRepo.existsByEmail(authRqDto.username())) {
            log.info("User with email: {} already exists", authRqDto.username());
            throw new UserAlreadyExistsException(String.format("User with email: %s already exists", authRqDto.username()));
        }
        AuthRqDto encryptUser = authRqDto.encodePassword(bCryptPasswordEncoder);
        User saveUser = userRepo.save(authRqDtoMapper.toEntity(encryptUser));

        Session session = new Session(UUID.randomUUID(), saveUser.getId(), saveUser.getEmail());
        sessionRepo.save(session);//TODO is really need? i think no

        return new AuthWithSessionRsDto(saveUser.getEmail(), session.getId());
    }

    @Transactional
    public AuthWithSessionRsDto signIn(AuthRqDto authRqDto) {
        Optional<User> saveUser = userRepo.findByEmail(authRqDto.username());
        if (saveUser.isEmpty()) {
            log.info("User with email: {} doesn't exists", authRqDto.username());
            throw new BadCredentialsException(String.format("User with email: %s doesn't exists", authRqDto.username()));
        }
        if (!bCryptPasswordEncoder.matches(authRqDto.password(), saveUser.get().getPasswordHash())) {
            log.info("Invalid password. Email: {}", authRqDto.username());
            throw new BadCredentialsException(String.format("Invalid password. Email: %s", authRqDto.username()));
        }
        Session session = new Session(UUID.randomUUID(), saveUser.get().getId(), saveUser.get().getEmail());
        sessionRepo.save(session);

        return new AuthWithSessionRsDto(saveUser.get().getEmail(), session.getId());
    }

}
