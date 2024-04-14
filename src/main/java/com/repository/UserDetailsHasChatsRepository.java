package com.repository;

import com.model.UserDetailsHasChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsHasChatsRepository extends JpaRepository<UserDetailsHasChats,Integer> {
}
