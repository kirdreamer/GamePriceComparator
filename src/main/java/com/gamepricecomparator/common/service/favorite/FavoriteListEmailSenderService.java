package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.common.constant.Platfrom;
import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.repository.UserRepository;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteListEmailSenderService {
    private final JavaMailSender mailSender;
    private final FavoriteGameService favoriteGameService;
    private final GameService gameService;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Value("${email.price.alarm.path}")
    private String emailPriceAlarmBodyPath;

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
            Map<Platfrom, GameInfoResponse> gameInfos = new HashMap<>();

            if (checkIfPriceLow(game)) {
                List<String> emails = favoriteGameService.getAllEmailsByGame(game.getName());
                if (Objects.nonNull(game.getSteam()) && checkIfPriceLowBySteam(game))
                    gameInfos.put(Platfrom.STEAM, game.getSteam());
                if (Objects.nonNull(game.getGog()) && checkIfPriceLowByGog(game))
                    gameInfos.put(Platfrom.GOG, game.getGog());
                if (Objects.nonNull(game.getEgs()) && checkIfPriceLowByEgs(game))
                    gameInfos.put(Platfrom.EPIC_GAMES_STORE, game.getEgs());
                sendPriceAlarmEmailsWithBodyFromPath(emails, emailPriceAlarmBodyPath, gameInfos, game.getName());
            }
        }
    }

    private String prepareMessageBody(String messageBody, Map.Entry<Platfrom, GameInfoResponse> gameInfo) {
        return messageBody
                .replaceFirst("\\{platform}", gameInfo.getKey().toString() + ", {platform}")
                .replaceFirst("Price at \\{platform}", "Price at " + gameInfo.getKey().toString())
                .replaceFirst("\\{price}",
                        gameInfo.getValue().price().final_value().toString() +
                                " " + gameInfo.getValue().price().currency() + "\nPrice at {platform}: {price}")
                .replaceFirst("Link to the platform \\{platform}", "Link to the platform " + gameInfo.getKey().toString())
                .replaceFirst("\\{link}", gameInfo.getValue().link() + "\nLink to the platform {platform}: {link}");
    }

    private String clearRedundantPartsOfMessageBody(String messageBody) {
        return messageBody
                .replaceFirst(", \\{platform}", "")
                .replaceFirst("Price at \\{platform}: \\{price}","")
                .replaceFirst("Link to the platform \\{platform}: \\{link}", "");
    }

    private void sendPriceAlarmEmailsWithBodyFromPath(
            List<String> emails,
            String emailBodyPath,
            Map<Platfrom, GameInfoResponse> gameInfos,
            String gameName) {
        String messageBody = "";
        try {
            messageBody = StreamUtils.copyToString(
                            new ClassPathResource(emailBodyPath).getInputStream(),
                            Charset.defaultCharset())
                    .replace("{name}", gameName);

            for (Map.Entry<Platfrom, GameInfoResponse> gameInfo : gameInfos.entrySet()) {
                messageBody = prepareMessageBody(messageBody, gameInfo);
            }
            messageBody = clearRedundantPartsOfMessageBody(messageBody);
        } catch (IOException e) {
            log.error("When sending a message and extracting the message body from the folder, " +
                    "an error occurred caused by: {}", e.getMessage());
        }

        log.info("Trying to send {} emails by game {}...", emails.size(), gameName);
        for (String email : emails) {
            String userName = "user";
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent())
                userName = user.get().getNickname();

            sendEmail(email, String.format("Price Alarm (%s)", gameName),
                    messageBody.replace("{user}", userName));
        }
        log.info("Emails with game {} were successfully sent", gameName);
    }

    public boolean checkIfPriceLow(GameResponse game) {
        return checkIfPriceLowBySteam(game) || checkIfPriceLowByGog(game) || checkIfPriceLowByEgs(game);
    }

    public boolean checkIfPriceLowBySteam(GameResponse game) {
        if (Objects.nonNull(game.getSteam()))
            return Double.compare(game.getSteam().price().final_value(), game.getSteam().price().initial_value()) < 0;
        return false;
    }

    public boolean checkIfPriceLowByGog(GameResponse game) {
        if (Objects.nonNull(game.getGog()))
            return Double.compare(game.getGog().price().final_value(), game.getGog().price().initial_value()) < 0;
        return false;
    }

    public boolean checkIfPriceLowByEgs(GameResponse game) {
        if (Objects.nonNull(game.getEgs()))
            return Double.compare(game.getEgs().price().final_value(), game.getEgs().price().initial_value()) < 0;
        return false;
    }

    //TODO ThreadExecutor
}
