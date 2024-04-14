package com.repository;

import com.model.MessagesOfChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessagesOfChat,Integer> {
}
