package com.aanovik42.smartmemecreatorbot.repository;

import com.aanovik42.smartmemecreatorbot.entity.Chat;
import com.google.common.cache.Cache;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatRepoImpl implements ChatRepo {

    private final Cache<String, Chat> cache;

    public ChatRepoImpl(Cache<String, Chat> cache) {
        this.cache = cache;
    }

    @Override
    public Optional<Chat> findChatByChatId(String chatId) {

        Chat chat = cache.getIfPresent(chatId);
        return Optional.ofNullable(chat);
    }

    @Override
    public Chat save(Chat chat) {

        String chatId = chat.getChatId();
        cache.put(chatId, chat);
        return chat;
    }
}
