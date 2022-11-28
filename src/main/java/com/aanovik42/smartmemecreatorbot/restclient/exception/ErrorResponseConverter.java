package com.aanovik42.smartmemecreatorbot.restclient.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseConverter {

    private final ObjectMapper objectMapper;

    public ErrorResponseConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ErrorResponse convertErrorResponseJsonToPojo(String jsonResponse) throws RuntimeException {
        try {
            return objectMapper.readValue(jsonResponse, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
