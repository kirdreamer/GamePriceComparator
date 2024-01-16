package com.gamepricecomparator.common.threadexecutor;

import com.gamepricecomparator.common.service.game.api.steam.impl.SteamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SteamRequestExecutor extends BaseExecutor implements Runnable {
    private final SteamService steamService;

    @Value("#{${steam.request.interval}}")
    private int interval;

    @Override
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        log.info("Thread SteamRequestExecutor is starting...");
        setRunning(true);
        log.info("Thread has been started");

        while (isRunning()) {
            try {
                steamService.getAllSteamGamesAndSaveIntoDatabase();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                log.info("Thread SteamRequestExecutor was interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
}
