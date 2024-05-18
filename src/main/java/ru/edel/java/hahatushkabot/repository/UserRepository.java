package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.edel.java.hahatushkabot.model.User;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
