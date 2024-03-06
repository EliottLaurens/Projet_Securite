package fr.limayrac.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String token;
    private LocalDateTime expiryDate;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
