package ru.nersus.storage.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.dto.AuthRsDto;
import ru.nersus.storage.entity.User;
import ru.nersus.storage.exception.UserAlreadyExistsException;
import ru.nersus.storage.mapper.AuthRqDtoMapper;
import ru.nersus.storage.repo.AuthRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {

    AuthRepo authRepo;
    AuthRqDtoMapper authRqDtoMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthRsDto signUp(AuthRqDto authRqDto) {
        if (authRepo.existsByEmail(authRqDto.username())) {
            log.info("User with email: {} already exists", authRqDto.username());
            throw new UserAlreadyExistsException(String.format("User with email: %s already exists", authRqDto.username()));
        }
        AuthRqDto encryptUser = authRqDto.encodePassword(bCryptPasswordEncoder);
        User saveUser = authRepo.save(authRqDtoMapper.toEntity(encryptUser));
        return new AuthRsDto(saveUser.getEmail());
    }

    public AuthRsDto signIn(AuthRqDto authRqDto) {
        Optional<User> saveUser = authRepo.findByEmail(authRqDto.username());
        if (saveUser.isEmpty()) {
            log.info("User with email: {} doesn't exists", authRqDto.username());
            throw new BadCredentialsException(String.format("User with email: %s doesn't exists", authRqDto.username()));
        }
        if (!bCryptPasswordEncoder.matches(authRqDto.password(), saveUser.get().getPasswordHash())) {
            log.info("Invalid password. Email: {}", authRqDto.username());
            throw new BadCredentialsException(String.format("Invalid password. Email: %s", authRqDto.username()));
        }
        return new AuthRsDto(authRqDto.username());
    }

}
