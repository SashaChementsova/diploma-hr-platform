package com.service.serviceImpl;

import com.model.Chat;
import com.repository.ChatRepository;
import com.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }
    @Override
    public Chat addAndUpdateChat(Chat chatEntity){
        return chatRepository.save(chatEntity);
    }
    @Override
    public List<Chat> getChats(){
        return chatRepository.findAll();
    }
    @Override
    public Chat findChatById(int id){

        return chatRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteChat(int id){
        chatRepository.deleteById(id);
    }
}
