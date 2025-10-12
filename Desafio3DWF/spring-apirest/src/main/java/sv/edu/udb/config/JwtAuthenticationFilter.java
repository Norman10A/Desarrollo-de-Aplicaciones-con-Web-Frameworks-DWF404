package sv.edu.udb.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sv.edu.udb.model.User;
import sv.edu.udb.repository.UserRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtils.validateJwtToken(token)) {
                String username = jwtUtils.getUsernameFromJwt(token);
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.name()))
                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
