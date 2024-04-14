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
public class LevelLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLevelLanguage;

    @Column(nullable=false, unique=true)
    private String level;
    @OneToMany(mappedBy = "levelLanguage")
    private List<Language> languageEntities;

    public LevelLanguage(String level) {
        this.level = level;
    }
}
