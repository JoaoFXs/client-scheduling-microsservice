package br.com.joaofxs.client_scheduling_microsservice.core.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationComponent {

    @Value("${app.host}")
    private String hostApp;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String email, String token){
        String resetUrl = hostApp + "/forgot-password?token=" + token;
        String body = "Click the link below to reset your password.\n" + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText(body);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

}
