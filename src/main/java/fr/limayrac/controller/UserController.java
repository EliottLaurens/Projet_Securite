package fr.limayrac.controller;

import fr.limayrac.dto.UserCreationDTO;
import fr.limayrac.dto.UserUpdateDTO;
import fr.limayrac.model.ApplicationUser;
import fr.limayrac.model.Role;
import fr.limayrac.repository.ApplicationUserRepository;
import fr.limayrac.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // Créer un utilisateur
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationUser> createUser(@RequestBody UserCreationDTO userDTO) {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail()); // Facultatif
        user.setPhoneNumber(userDTO.getPhoneNumber()); // Facultatif

        Set<Role> userRoles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found.")))
                .collect(Collectors.toSet());

        user.setRoles(userRoles);
        ApplicationUser newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    // Modifier un utilisateur
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationUser> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userDetails) {
        ApplicationUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Applique les changements depuis userDetails au user existant si la valeur est non nulle
        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword())); // Assure-toi d'encoder le nouveau mot de passe
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }
        if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            Set<Role> userRoles = userDetails.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found.")))
                    .collect(Collectors.toSet());
            user.setRoles(userRoles);
        }

        final ApplicationUser updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
