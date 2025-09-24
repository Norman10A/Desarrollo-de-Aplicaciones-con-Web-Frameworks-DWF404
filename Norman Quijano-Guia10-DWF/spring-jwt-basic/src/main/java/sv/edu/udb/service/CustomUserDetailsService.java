package sv.edu.udb.service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("Cargando usuario: " + username);
        if ("testuser".equals(username)) {
            System.out.println("Usuario encontrado: testuser");
            return new User(
                    "testuser",
                    "testpassword", // Contraseña sin cifrado para pruebas
                    new ArrayList<>()
            );
        }
        System.out.println("Usuario no encontrado: " + username);
        throw new RuntimeException("User not found");
    }
}