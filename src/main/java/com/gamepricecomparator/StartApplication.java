package com.gamepricecomparator;

import com.gamepricecomparator.common.component.SteamRequestExecutor;
import com.gamepricecomparator.common.service.favorite.FavoriteListEmailSenderService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
public class StartApplication {
    private final SteamRequestExecutor steamRequestExecutor;
    private final FavoriteListEmailSenderService favoriteListEmailSenderService;

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runSteamRequestExecutor() {
        steamRequestExecutor.start();
    }

    @PreDestroy
    public void stopSteamRequestExecute() {
        steamRequestExecutor.interrupt();
    }
}
