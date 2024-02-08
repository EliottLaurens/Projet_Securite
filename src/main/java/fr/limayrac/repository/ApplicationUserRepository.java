package fr.limayrac.repository;

import fr.limayrac.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
}
