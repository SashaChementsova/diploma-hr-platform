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
public class LanguageTestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLanguageTestQuestion;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String rightAnswer;
    @Column(nullable = false)
    private String answer2;
    @Column(nullable = false)
    private String answer3;
    @Column(nullable = false)
    private String answer4;
    @Column(nullable = false)
    private float point;
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;
    @OneToMany(mappedBy = "languageTestQuestion")
    private List<LanguageTestHasQuestion> languageTestHasQuestionEntities;
}
