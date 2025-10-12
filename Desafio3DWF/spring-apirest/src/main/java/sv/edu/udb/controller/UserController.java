package sv.edu.udb.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.dto.UserDTO;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listAll() {
        var users = userRepository.findAll()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> me(@RequestHeader("Authorization") String authHeader) {
        // authHeader is "Bearer <token>" — but we can get username from security context
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        var userOpt = userRepository.findByUsername(username);
        return userOpt.map(u -> ResponseEntity.ok(new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRoles())))
                .orElse(ResponseEntity.notFound().build());
    }
}
