package fr.limayrac.config;

import fr.limayrac.model.ApplicationUser;
import fr.limayrac.model.Role;
import fr.limayrac.repository.ApplicationUserRepository;
import fr.limayrac.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Set<String> adminRoles = new HashSet<>(Collections.singletonList("ROLE_ADMIN"));
        createUserIfNotFound("admin", "admin", adminRoles);

        alreadySetup = true;
    }


    private Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name).orElse(null); // Assure-toi d'avoir cette méthode dans ton RoleRepository
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
        return role;
    }

    private ApplicationUser createUserIfNotFound(String username, String password, Set<String> roleNames) {
        ApplicationUser user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            user = new ApplicationUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));

            Set<Role> roles = new HashSet<>();
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            return roleRepository.save(newRole); // Sauvegarde le nouveau rôle si pas trouvé
                        });
                roles.add(role); // Utilise le rôle existant ou le nouveau
            }
            user.setRoles(roles);
            userRepository.save(user);
        }
        return user;
    }
}
