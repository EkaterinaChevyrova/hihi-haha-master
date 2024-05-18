package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.edel.java.hahatushkabot.model.JokesModel;

import java.util.Optional;

public interface JokesRepository extends JpaRepository<JokesModel, Long> {
    Optional<JokesModel> findById(Long id);
}
