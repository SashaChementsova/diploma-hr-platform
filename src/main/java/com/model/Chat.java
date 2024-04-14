package com.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idChat;
    @Column(nullable = false)
    private Date dateOfCreation;
    @OneToMany(mappedBy = "chat")
    private List<MessagesOfChat> messageEntities;

    @OneToMany(mappedBy = "chat")
    private List<UserDetailsHasChats> userDetailsHasChatsEntities;

}
