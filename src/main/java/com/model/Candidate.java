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
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCandidate;

    @OneToOne(fetch = FetchType.LAZY)
    private UserDetail userDetail;

    @OneToOne(fetch = FetchType.LAZY)
    private Background background;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="CandidateHasLanguages",
            joinColumns={@JoinColumn(name="CANDIDATE_ID", referencedColumnName="idCandidate")},
            inverseJoinColumns={@JoinColumn(name="LANGUAGE_ID", referencedColumnName="idLanguage")})
    private List<Language> languages = new ArrayList<>();

    @OneToMany(mappedBy = "candidate")
    private List<Education> educationEntities;

    @OneToMany(mappedBy = "candidate")
    private List<Trial> trialEntities;
}
