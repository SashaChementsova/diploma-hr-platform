package com.model;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LanguageTestHasQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLanguageTestHasQuestionEntity;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageTest languageTest;
    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageTestQuestion languageTestQuestion;
}
