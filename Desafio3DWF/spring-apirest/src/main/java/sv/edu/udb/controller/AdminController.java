package sv.edu.udb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ping")
    public Map<String, Object> ping(Authentication auth) {
        return Map.of(
                "ok", true,
                "who", auth.getName(),
                "roles", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }
}
