package com.gamepricecomparator.common.service.favorite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteListEmailSenderService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmail(String[] recipientEmails, String subject, String body) {
        log.info("Trying to send message to {} emails with subject {}...", recipientEmails.length, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(recipientEmails);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        log.info("Email with subject {} was successfully sent", subject);
    }
}
