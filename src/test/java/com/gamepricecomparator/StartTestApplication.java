package com.gamepricecomparator;

import com.gamepricecomparator.common.threadexecutor.SteamRequestExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("integration-test")
@RequiredArgsConstructor
public class StartTestApplication {
    private final SteamRequestExecutor steamRequestExecutor;

    public static void main(String[] args) {
        SpringApplication.run(StartTestApplication.class, args);
    }
}
