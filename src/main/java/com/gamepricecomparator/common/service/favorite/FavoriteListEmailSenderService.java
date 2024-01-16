package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.common.service.game.GameService;
import com.gamepricecomparator.common.web.response.game.GameInfoResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
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
import java.util.ArrayList;
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

    @Value("${email.price.alarm.gog.path}")
    private String emailPriceAlarmGogBodyPath;

    @Value("${email.price.alarm.steam.gog.path}")
    private String emailPriceAlarmSteamGogBodyPath;

    @Value("${email.price.alarm.steam.path}")
    private String emailPriceAlarmSteamBodyPath;

    public void sendEmail(String recipientEmails, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(recipientEmails);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }

    public void sendPriceAlarmEmail() {
        for (GameResponse game :
                gameService.getSpecificGamesListFromList(favoriteGameService.getAllFavoriteGames())) {
            List<GameInfoResponse> gameInfos = new ArrayList<>();

            if (checkIfPriceLowBySteam(game) && checkIfPriceLowByGog(game)) {
                List<String> emails = favoriteGameService.getAllEmailsByGame(game.getName());
                gameInfos.add(game.getSteam());
                gameInfos.add(game.getGog());
                sendPriceAlarmEmailsWithBodyFromPath(emails, emailPriceAlarmSteamGogBodyPath, gameInfos, game.getName());
                return;
            }
            if (checkIfPriceLowBySteam(game)) {
                List<String> emails = favoriteGameService.getAllEmailsByGame(game.getName());
                gameInfos.add(game.getSteam());
                sendPriceAlarmEmailsWithBodyFromPath(emails, emailPriceAlarmSteamBodyPath, gameInfos, game.getName());
                return;
            }
            if (checkIfPriceLowByGog(game)) {
                List<String> emails = favoriteGameService.getAllEmailsByGame(game.getName());
                gameInfos.add(game.getGog());
                sendPriceAlarmEmailsWithBodyFromPath(emails, emailPriceAlarmGogBodyPath, gameInfos, game.getName());
                return;
            }
        }
    }

    private void sendPriceAlarmEmailsWithBodyFromPath(
            List<String> emails,
            String emailBodyPath,
            List<GameInfoResponse> gameInfos,
            String gameName) {
        String messageBody = "";
        try {
            messageBody = StreamUtils.copyToString(
                    new ClassPathResource(emailBodyPath).getInputStream(),
                    Charset.defaultCharset()).replace("{name}", gameName);

            for (GameInfoResponse gameInfo : gameInfos) {
                messageBody = messageBody
                        .replace("{price}",
                                gameInfo.price().final_value().toString() +
                                        " " + gameInfo.price().currency())
                        .replace("{link}", gameInfo.link());
            }
        } catch (IOException e) {
            log.error("When sending a message and extracting the message body from the folder, " +
                    "an error occurred caused by: {}", e.getMessage());
        }

        log.info("Trying to send {} emails by game {}...", emails.size(), gameName);
        for (String email : emails) {
            sendEmail(email, String.format("Price Alarm (%s)", gameName), messageBody);
        }
        log.info("Emails with game {} were successfully sent", gameName);
    }

    public boolean checkIfPriceLowBySteam(GameResponse game) {
        return Double.compare(game.getSteam().price().final_value(), game.getSteam().price().initial_value()) < 0;
    }

    public boolean checkIfPriceLowByGog(GameResponse game) {
        return Double.compare(game.getGog().price().final_value(), game.getGog().price().initial_value()) < 0;
    }

    //TODO ThreadExecutor
}
