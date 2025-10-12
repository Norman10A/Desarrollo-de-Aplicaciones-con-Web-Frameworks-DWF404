package sv.edu.udb.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.User;
import sv.edu.udb.model.Role;
import sv.edu.udb.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(String username, String email, String password, Set<String> strRoles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(Role.ROLE_USER);
        } else {
            strRoles.forEach(r -> {
                if (r.equalsIgnoreCase("admin")) roles.add(Role.ROLE_ADMIN);
                else roles.add(Role.ROLE_USER);
            });
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUserFromOAuth(String username, String email, String provider) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        // Un password random placeholder (no login local) - must be non-null
        u.setPassword(passwordEncoder.encode("oauth2user-default-password"));
        u.setProvider(provider);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        u.setRoles(roles);
        return userRepository.save(u);
    }
}
