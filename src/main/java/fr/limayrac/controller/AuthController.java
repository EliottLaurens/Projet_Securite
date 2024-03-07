package fr.limayrac.controller;

import fr.limayrac.dto.VerificationRequest;
import fr.limayrac.model.JwtResponse;
import fr.limayrac.model.LoginRequest;
import fr.limayrac.model.VerificationToken;
import fr.limayrac.repository.VerificationTokenRepository;
import fr.limayrac.security.JwtTokenProvider;
import fr.limayrac.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService mailService;

    @Autowired
    private UserDetailsService userDetailsService;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Envoie du code de vérification
        sendVerificationCode(userDetails);

        return ResponseEntity.ok("Verification code sent to your email. Please verify to complete signin.");
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        VerificationToken token = verificationTokenRepository.findByToken(verificationRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification code."));

        if (token.getUsername().equals(verificationRequest.getUsername())
                && token.getExpiryDate().isAfter(LocalDateTime.now())) {
            // Le token est valide, génère le JWT final pour l'utilisateur
            UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());

// Crée un Authentication à partir de UserDetails
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            String jwt = tokenProvider.generateToken(authentication);


            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getUsername()));
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification code.");
        }
    }



    public void sendVerificationCode(UserDetails userDetails) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUsername(userDetails.getUsername());
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15)); // Token valide pour 15 minutes
        verificationTokenRepository.save(verificationToken);

        // Envoi du mail avec le token
        mailService.sendVerificationCode(userDetails, token);
    }

}
