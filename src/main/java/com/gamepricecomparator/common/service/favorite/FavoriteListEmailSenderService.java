package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.common.dto.projection.FavoriteGameInfoDTO;
import com.gamepricecomparator.common.service.game.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteListEmailSenderService {
    private final JavaMailSender mailSender;
    private final FavoriteGameService favoriteGameService;
    private final GameService gameService;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Value("${email.price.alarm.path}")
    private String emailPriceAlarmBodyPath;

    public void sendEmail(String[] recipientEmails, String subject, String body) {
        log.info("Trying to send message to {} emails with subject {}...", recipientEmails.length, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(recipientEmails);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        log.info("Email with subject {} was successfully sent", subject);
    }

    public void sendPriceAlarmEmail() {
        List<FavoriteGameInfoDTO> favoriteGames = favoriteGameService.getAllFavoriteGames();

        for (FavoriteGameInfoDTO game : favoriteGames) {
            if (checkIfPriceLow()) {
                log.info("Trying to send emails by game {}", game.name());
                try {
                    sendEmail(favoriteGameService.getAllEmailsByGame(game.name()),
                            String.format("Price Alarm (%s)", game.name()),
                            StreamUtils.copyToString(
                                    new ClassPathResource(emailPriceAlarmBodyPath).getInputStream(),
                                    Charset.defaultCharset()).replace("{name}", game.name())
                    );
                } catch (IOException e) {
                    log.error("When sending a message and extracting the message body from the folder, " +
                            "an error occurred caused by: {}", e.getMessage());
                }
            }
        }
    }

    public boolean checkIfPriceLow() {
        return true;
    }
}
