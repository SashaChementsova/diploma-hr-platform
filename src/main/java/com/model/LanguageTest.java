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
public class LanguageTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLanguageTest;
    //@Column(nullable = false)
    private Date date;
    @OneToOne( fetch = FetchType.LAZY)
    private Result result;

    @OneToMany(mappedBy = "languageTest")
    private List<LanguageTestHasQuestion> languageTestHasQuestionEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    private ResultTesting resultTesting;

}
