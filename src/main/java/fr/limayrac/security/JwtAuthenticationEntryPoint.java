package fr.limayrac.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Ceci est invoqué quand l'utilisateur tente d'accéder à une ressource sécurisée sans fournir de credentials
        // On envoie juste une réponse de statut 401 indiquant que l'accès est refusé
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur : Non autorisé");
    }
}
