package fr.limayrac.model;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
        this.username = username;
    }

    // Getters and Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Vous pouvez ajouter d'autres propriétés selon les besoins, comme l'expiration du token, les rôles, etc.
}
