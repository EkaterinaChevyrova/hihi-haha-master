package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.edel.java.hahatushkabot.model.UserRole;


public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
}
