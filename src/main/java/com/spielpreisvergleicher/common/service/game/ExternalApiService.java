package com.spielpreisvergleicher.common.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiService {
    private final RestTemplate restTemplate;

    public <T> T makeGetRequest(String url, Class<T> requiredClassType) {
        log.info("By url {} was sent GET request", url);
        return restTemplate.getForObject(url, requiredClassType);
    }
}
