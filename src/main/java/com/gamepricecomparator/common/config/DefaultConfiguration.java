package com.gamepricecomparator.common.config;

import com.gamepricecomparator.common.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("!integration-test")
@PropertySource("classpath:gog.properties")
@PropertySource("classpath:egs.properties")
@PropertySource("classpath:steam.properties")
@PropertySource("classpath:email.properties")
public class DefaultConfiguration extends ApplicationConfig {
    public DefaultConfiguration(UserRepository userRepository) {
        super(userRepository);
    }
}
