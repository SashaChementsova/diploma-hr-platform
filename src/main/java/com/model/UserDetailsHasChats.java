package com.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserDetailsHasChats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserDetailsHasChats;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserDetail userDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;
}
