package sv.edu.udb.dto;
public class AuthResponse {
    private String token;
    // Constructor
    public AuthResponse(String token, String refreshToken) {
        this.token = token;
    }
    // Getter y Setter
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}