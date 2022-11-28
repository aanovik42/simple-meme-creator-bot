package com.aanovik42.smartmemecreatorbot.processor;

import com.aanovik42.smartmemecreatorbot.handler.CallbackQueryHandler;
import com.aanovik42.smartmemecreatorbot.handler.MessageHandler;
import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ProcessorImpl implements Processor {

    private final CallbackQueryHandler callbackQueryHandler;
    private final MessageHandler messageHandler;

    public ProcessorImpl(CallbackQueryHandler callbackQueryHandler, MessageHandler messageHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public HandlerResponse executeMessage(Message message) {
        return messageHandler.choose(message);
    }

    @Override
    public HandlerResponse executeCallBackQuery(CallbackQuery callbackQuery) {
        return callbackQueryHandler.choose(callbackQuery);
    }
}
