package ru.edel.java.hahatushkabot.server;

import ru.edel.java.hahatushkabot.model.User;
import ru.edel.java.hahatushkabot.model.UserAuthority;

import java.util.List;

public interface UserService {

    void registration(String username, String password);
    User getUserById(Long userId);
    void updateUserRoles(Long userId, List<UserAuthority> authorities);
}
