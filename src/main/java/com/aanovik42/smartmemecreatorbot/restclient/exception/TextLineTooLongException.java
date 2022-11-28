package com.aanovik42.smartmemecreatorbot.restclient.exception;

public class TextLineTooLongException extends RuntimeException {

    private final String formattedMessage;

    public TextLineTooLongException(String message, String formattedMessage) {
        super(message);
        this.formattedMessage = formattedMessage;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }
}
