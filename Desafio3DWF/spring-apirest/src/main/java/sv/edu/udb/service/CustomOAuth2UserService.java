package sv.edu.udb.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.model.User;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // You can process attributes here if needed
        return oauth2User;
    }

    public User processOAuthPostLogin(Map<String, Object> attributes, String provider) {
        // GitHub usually provides "login" (username) and "email" maybe null (use extra endpoint)
        String username = (String) attributes.getOrDefault("login", attributes.get("name"));
        String email = (String) attributes.get("email");
        if (email == null) {
            // sometimes GitHub doesn't provide email publicly; you may need to call emails endpoint
            email = username + "@github.com"; // placeholder
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            return userService.createUserFromOAuth(username, email, provider);
        }
    }
}

