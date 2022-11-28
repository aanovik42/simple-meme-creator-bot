package com.aanovik42.smartmemecreatorbot.handler;

import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Handler<T> {
    HandlerResponse choose(T t);
}
