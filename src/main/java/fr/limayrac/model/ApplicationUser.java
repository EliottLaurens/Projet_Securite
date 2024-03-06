package fr.limayrac.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phoneNumber;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // getters and setters

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    // Override de equals et hashCode si nécessaire, surtout si on utilise des collections
    // qui dépendent de l'égalité des objets, comme Set.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationUser)) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    // ToString si nécessaire pour le débogage

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
