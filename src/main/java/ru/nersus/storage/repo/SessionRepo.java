package ru.nersus.storage.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nersus.storage.entity.Session;

@Repository
public interface SessionRepo extends CrudRepository<Session, String> {
}
