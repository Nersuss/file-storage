package ru.nersus.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nersus.storage.entity.User;

import java.util.Optional;

public interface AuthRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
