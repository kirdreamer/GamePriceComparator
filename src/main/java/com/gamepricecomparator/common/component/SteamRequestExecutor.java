package com.gamepricecomparator.common.component;

import com.gamepricecomparator.common.service.game.api.steam.impl.SteamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class SteamRequestExecutor implements Runnable {
    private final SteamService steamService;
    private Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(true);

    @Value("#{${steam.request.interval}}")
    private int interval;

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void interrupt() {
        running.set(false);
        thread.interrupt();
    }

    boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        log.info("Thread SteamRequestExecutor is starting...");
        running.set(true);
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
