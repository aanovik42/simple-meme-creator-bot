package com.aanovik42.smartmemecreatorbot.processor;

import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Processor {

    HandlerResponse executeMessage(Message message);

    HandlerResponse executeCallBackQuery(CallbackQuery callbackQuery);

    default HandlerResponse process(Update update) {
        HandlerResponse handlerResponse = new HandlerResponse();
        if (update.hasMessage()) {
            handlerResponse = executeMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handlerResponse = executeCallBackQuery(update.getCallbackQuery());
        }

        return handlerResponse;
    }
}
