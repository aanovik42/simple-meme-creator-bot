package com.aanovik42.smartmemecreatorbot.entity;

import com.aanovik42.smartmemecreatorbot.entity.enums.ChatState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    private String chatId;
    private ChatState chatState;
    private MemeTemplate memeTemplate;

    public Chat(String chatId, ChatState chatState) {
        this.chatId = chatId;
        this.chatState = chatState;
    }
}
