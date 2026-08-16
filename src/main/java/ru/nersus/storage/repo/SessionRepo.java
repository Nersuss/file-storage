package ru.nersus.storage.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nersus.storage.entity.Session;

import java.util.Optional;

@Repository
public interface SessionRepo extends CrudRepository<Session, String> {
    Optional<Session> findByIdContaining(String id);
}
