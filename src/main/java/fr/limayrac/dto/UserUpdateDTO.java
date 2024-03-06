package fr.limayrac.dto;

import java.util.Set;

public class UserUpdateDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Set<String> roles;

    // Getter et Setter pour username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter et Setter pour password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter et Setter pour email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter et Setter pour phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter et Setter pour roles
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
