package com.service;

import com.model.MessagesOfChat;

import java.util.List;

public interface MessageService {
    public MessagesOfChat addAndUpdateMessage(MessagesOfChat messagesOfChat);
    public List<MessagesOfChat> getMessages();

    public MessagesOfChat findMessageById(int id);
    public void deleteMessage(int id);
}
