package sv.edu.udb.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;


    @Value("${app.oauth2.success-redirect-url:}")
    private String successRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1) Usuario desde GitHub (login/email) o fallback a authentication.getName()
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User oAuth2User) {
            username = oAuth2User.getAttribute("login");
            if (!StringUtils.hasText(username)) {
                username = oAuth2User.getAttribute("email");
            }
            if (!StringUtils.hasText(username)) {
                username = authentication.getName();
            }
        } else {
            username = authentication.getName();
        }


        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        String token = jwtUtils.generateToken(username, roles);


        if (StringUtils.hasText(successRedirectUrl)) {
            String url = UriComponentsBuilder.fromUriString(successRedirectUrl)
                    .queryParam("token", token)
                    .build(true)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, url);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }
}
