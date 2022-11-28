package com.aanovik42.smartmemecreatorbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Configuration
public class RestClientConfig {

    private final String apiUrl;

    public RestClientConfig(@Value("${api.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public Traverson getTraverson() {
        Traverson traverson = new Traverson(URI.create(apiUrl), MediaTypes.HAL_JSON);
        return traverson;
    }
}
