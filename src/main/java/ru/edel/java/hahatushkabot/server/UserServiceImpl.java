package ru.edel.java.hahatushkabot.server;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edel.java.hahatushkabot.model.User;
import ru.edel.java.hahatushkabot.model.UserAuthority;
import ru.edel.java.hahatushkabot.model.UserRole;
import ru.edel.java.hahatushkabot.repository.UserRepository;
import ru.edel.java.hahatushkabot.repository.UserRolesRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registration(String username, String password) {

            User user = userRepository.save(
                    new User()
                            .setId(null)
                            .setUsername(username)
                            .setPassword(passwordEncoder.encode(password))
                            .setLocked(false)
                            .setExpired(false)
                            .setEnabled(true)
            );
            userRolesRepository.save(new UserRole(null, UserAuthority.USER, user));
        }
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
    }

    @Override
    public void updateUserRoles(Long userId, List<UserAuthority> authorities) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
        List<UserRole> userRoles = authorities.stream()
                .map(authority -> new UserRole(null, authority, user))
                .collect(Collectors.toList());
        userRolesRepository.saveAll(userRoles);
    }
}
