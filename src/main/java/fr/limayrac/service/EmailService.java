package fr.limayrac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("eliott.laurens@limayrac.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerificationCode(UserDetails userDetails, String verificationCode) {
        String subject = "Code de vérification";
        String text = "Votre code de vérification est : " + verificationCode;

        // Appelle directement la méthode sendSimpleMessage sans auto-injection
        sendSimpleMessage(userDetails.getUsername(), subject, text);
    }
}
