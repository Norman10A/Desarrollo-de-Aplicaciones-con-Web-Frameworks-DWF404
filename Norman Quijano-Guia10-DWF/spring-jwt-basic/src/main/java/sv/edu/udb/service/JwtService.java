package sv.edu.udb.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
@Service
public class JwtService {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora
    // Generar un token a partir del username
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }
    // Validar que el token corresponde al usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    // Extraer username desde el token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // Verificar si ya expiró el token
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Obtener fecha de expiración
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // Extraer un claim específico
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}

