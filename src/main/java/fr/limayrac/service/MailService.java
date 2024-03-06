package fr.limayrac.service;

public interface MailService {
    void sendVerificationMail(String to, String verificationCode);
}
