package com.service.serviceImpl;

import com.model.MessagesOfChat;
import com.repository.MessageRepository;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    @Override
    public MessagesOfChat addAndUpdateMessage(MessagesOfChat messagesOfChat){
        return messageRepository.save(messagesOfChat);
    }
    @Override
    public List<MessagesOfChat> getMessages(){
        return messageRepository.findAll();
    }
    @Override
    public MessagesOfChat findMessageById(int id){

        return messageRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteMessage(int id){
        messageRepository.deleteById(id);
    }
}
