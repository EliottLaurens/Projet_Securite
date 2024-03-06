package fr.limayrac.service;


import org.springframework.stereotype.Component;

@Component
public class SimpleMailService implements MailService {

    // Utilise JavaMailSender ou un autre client mail pour l'implémentation réelle

    @Override
    public void sendVerificationMail(String to, String verificationCode) {
        System.out.println("Envoi du code de vérification " + verificationCode + " à " + to);
        // Implémente l'envoi d'email ici
    }
}
