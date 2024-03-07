package fr.limayrac.service;

import fr.limayrac.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    // Envoie un email HTML en utilisant un modèle
    public void sendHtmlMessage(String to, String subject, String verificationCode) throws MessagingException, IOException {
        String htmlTemplate = StreamUtils.copyToString(
                resourceLoader.getResource("classpath:templates/verificationEmail.html").getInputStream(),
                StandardCharsets.UTF_8);

        // Remplace les placeholders dans le modèle HTML
        String htmlContent = htmlTemplate.replace("${verificationCode}", verificationCode);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("eliott.laurens@limayrac.fr");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true pour indiquer que le contenu est HTML
        mailSender.send(message);
    }

    // Utilise le UserDetails pour trouver l'email et envoyer le code de vérification
    public void sendVerificationCode(UserDetails userDetails, String verificationCode) {
        String email = userDetailsService.findUserEmailByUsername(userDetails.getUsername());

        try {
            sendHtmlMessage(email, "Code de vérification", verificationCode);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            // Log l'erreur ou gère-la selon tes besoins
        }
    }
}
