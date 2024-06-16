package com.gamepricecomparator.common.config;

import com.gamepricecomparator.common.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("integration-test")
@PropertySource("classpath:gog-test.properties")
@PropertySource("classpath:egs-test.properties")
@PropertySource("classpath:steam-test.properties")
@PropertySource("classpath:email-test.properties")
public class TestConfiguration extends ApplicationConfig {
    public TestConfiguration(UserRepository userRepository) {
        super(userRepository);
    }
}
