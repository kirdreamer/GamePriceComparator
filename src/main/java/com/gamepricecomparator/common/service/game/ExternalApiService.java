package com.gamepricecomparator.common.service.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamepricecomparator.common.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public <T> T makeGetRequest(String url, Class<T> requiredClassType) {
        log.info("By url {} was sent GET request", url);
        return restTemplate.getForObject(url, requiredClassType);
    }

    public <T> T getJsonFromApiAndFindObjectByName(String url, Class<T> requiredClassType, String objectName) {
        try {
            return objectMapper.treeToValue(
                    objectMapper.readTree(makeGetRequest(url, String.class)).findValue(objectName),
                    requiredClassType
            );
        } catch (JsonProcessingException e) {
            log.error("An error occurs during the mapping: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (HttpClientErrorException e) {
            log.error("An error occurred during the mapping: {}", e.getMessage());
            throw new HttpException(e.getStatusCode().value(), e.getMessage());
        }
    }
}
