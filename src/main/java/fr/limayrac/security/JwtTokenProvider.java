package fr.limayrac.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            // Log & handle signature exception
        } catch (MalformedJwtException e) {
            // Log & handle malformed JWT
        } catch (ExpiredJwtException e) {
            // Log & handle expired JWT
        } catch (UnsupportedJwtException e) {
            // Log & handle unsupported JWT
        } catch (IllegalArgumentException e) {
            // Log & handle illegal argument
        }
        return false;
    }

    public String getUserNameFromJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
