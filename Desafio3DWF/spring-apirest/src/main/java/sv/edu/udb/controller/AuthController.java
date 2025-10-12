package sv.edu.udb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.LoginRequest;
import sv.edu.udb.dto.SignupRequest;
import sv.edu.udb.dto.UserDTO;
import sv.edu.udb.model.User;
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.service.UserService;
import sv.edu.udb.config.JwtUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, UserRepository userRepository, JwtUtils jwtUtils, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Username is already taken!"));
        }
        if (signUpRequest.getEmail() != null && userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use!"));
        }

        User user = userService.registerUser(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getRoles());
        UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }
        List<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toList());
        String token = jwtUtils.generateToken(user.getUsername(), roles);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
        return ResponseEntity.ok(resp);
    }
}
