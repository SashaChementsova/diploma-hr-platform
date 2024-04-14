package com.service.serviceImpl;

import com.model.*;
import com.repository.MessageRepository;
import com.repository.UserDetailsHasChatsRepository;
import com.service.MessageService;
import com.service.UserDetailsHasChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDetailsHasChatsServiceImpl implements UserDetailsHasChatService {
    private final UserDetailsHasChatsRepository userDetailsHasChatsRepository;
    private final MessageRepository messageRepository;
    @Autowired
    public UserDetailsHasChatsServiceImpl(UserDetailsHasChatsRepository userDetailsHasChatsRepository,MessageRepository messageRepository){
        this.userDetailsHasChatsRepository = userDetailsHasChatsRepository;
        this.messageRepository=messageRepository;
    }
    @Override
    public UserDetailsHasChats addAndUpdateUserDetailsHasChat(UserDetailsHasChats userDetailsHasChats){
        return userDetailsHasChatsRepository.save(userDetailsHasChats);
    }
    @Override
    public List<UserDetailsHasChats> getUserDetailsHasChats(){
        return userDetailsHasChatsRepository.findAll();
    }

    @Override
    public UserDetailsHasChats findUserDetailsHasChatById(int id){

        return userDetailsHasChatsRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteUserDetailsHasChat(int id){
        userDetailsHasChatsRepository.deleteById(id);
    }

    @Override
    public void deleteChatByUserDetail(UserDetail userDetail){
        List<UserDetailsHasChats> userDetailsHasChats=userDetail.getUserDetailsHasChatsEntities();
        if(userDetailsHasChats!=null){
            if(!(userDetailsHasChats.isEmpty())){
                for(UserDetailsHasChats userDetailsHasChat:userDetailsHasChats){
                    List<MessagesOfChat> messages=userDetailsHasChat.getChat().getMessageEntities();
                    if(messages!=null){
                        if(!(messages.isEmpty())){
                            for(MessagesOfChat message:messages){
                                if(message.getIdSender()==userDetail.getIdUserDetails()){
                                    message.setIdSender(0);
                                    messageRepository.save(message);
                                }
                            }
                        }
                    }
                    userDetailsHasChat.setUserDetail(null);
                    addAndUpdateUserDetailsHasChat(userDetailsHasChat);
                }
            }
        }
    }
}
