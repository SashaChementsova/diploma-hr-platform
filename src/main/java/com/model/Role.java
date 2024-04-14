package com.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRole;

    @Column(nullable=false, unique=true)
    private String nameRole;

    @ManyToMany(mappedBy="roles")
    private List<UserDetail> userDetailsEntities;


}
