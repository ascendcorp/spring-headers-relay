package com.truemoney.api.springheadersrelay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    RestTemplate sampleRestTemplate () {
        return new RestTemplate();
    }
}
