package fr.limayrac.security;

import fr.limayrac.model.ApplicationUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.limayrac.repository.ApplicationUserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        // S'assure que les autorités ne sont pas nulles
        Collection<GrantedAuthority> authorities = Collections.emptyList();

        // Si tu as une logique pour extraire les véritables autorités de l'utilisateur, remplace
        // la collection vide par la collection d'autorités de l'utilisateur.

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities) // Utilise la collection garantie non nulle ici
                .build();
    }

    public String findUserEmailByUsername(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
        return user.getEmail();
    }
}
