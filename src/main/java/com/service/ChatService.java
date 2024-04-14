package com.service;

import com.model.Chat;

import java.util.List;

public interface ChatService {
    public Chat addAndUpdateChat(Chat chatEntity);
    public List<Chat> getChats();

    public Chat findChatById(int id);
    public void deleteChat(int id);
}
