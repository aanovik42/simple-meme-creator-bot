package com.aanovik42.smartmemecreatorbot.repository;

import com.aanovik42.smartmemecreatorbot.entity.Chat;

import java.util.Optional;

public interface ChatRepo {

    Optional<Chat> findChatByChatId(String chatId);
    Chat save(Chat chat);
}
