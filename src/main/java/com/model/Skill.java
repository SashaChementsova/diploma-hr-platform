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
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSkill;

    @Column(nullable=false, unique=true)
    private String nameSkill;

    @ManyToMany(mappedBy="skills")
    private List<Background> backgrounds;
}
