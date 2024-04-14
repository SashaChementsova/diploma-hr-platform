package com.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVacancy;
    @Column(nullable = false)
    private int freePositions;
    @Column(nullable = false)
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="VacancyHasLanguages",
            joinColumns={@JoinColumn(name="VACANCY_ID", referencedColumnName="idVacancy")},
            inverseJoinColumns={@JoinColumn(name="LANGUAGE_ID", referencedColumnName="idLanguage")})
    private List<Language> languages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private CityCompany cityCompany;

    @OneToOne( fetch = FetchType.LAZY)
    private Background background;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hr hr;

    @OneToMany(mappedBy = "vacancy")
    private List<Trial> trialEntities;


}
