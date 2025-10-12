package sv.edu.udb.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.service.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SegurityConfig {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Para Postman está bien deshabilitar CORS; si tendrás frontend, cámbialo a .cors()
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // públicos
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/public/**",
                                "/oauth2/**",
                                "/login/**",
                                "/error"
                        ).permitAll()
                        // ejemplo de área admin si usas @PreAuthorize en controladores
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // 👉 401 en JSON para cualquier /api/** sin credenciales (NO redirigir a GitHub)
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Forbidden\"}");
                        })
                )


                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler)
                )


                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);


        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

