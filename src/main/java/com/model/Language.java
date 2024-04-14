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
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLanguage;
    @ManyToOne(fetch = FetchType.LAZY)
    private LevelLanguage levelLanguage;
    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageName languageName;

    @ManyToMany(mappedBy="languages")
    private List<Candidate> candidateEntities;

    @ManyToMany(mappedBy="languages")
    private List<Employee> employeeEntities;

    @ManyToMany(mappedBy="languages")
    private List<Vacancy> vacancyEntities;
    @OneToMany(mappedBy = "language")
    private List<LanguageTestQuestion> languageTestQuestionEntities;

    @Override
    public String toString() {
        return languageName.getName()+" "+levelLanguage.getLevel();
    }
}
