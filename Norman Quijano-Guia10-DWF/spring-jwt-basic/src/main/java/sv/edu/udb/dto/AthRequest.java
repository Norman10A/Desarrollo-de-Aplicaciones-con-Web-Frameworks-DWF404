package sv.edu.udb.dto;
public class AthRequest {
    private String username;
    private String password;
    // Constructor vacío (requerido para deserialización JSON)
    public AthRequest() {}
    // Constructor con parámetros
    public AthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Getters y Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

