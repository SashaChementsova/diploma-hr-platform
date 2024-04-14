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
public class Background {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBackground;

    @Column(nullable=false)
    private int experience;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="BackgroundHasSkills",
            joinColumns={@JoinColumn(name="BACKGROUND_ID", referencedColumnName="idBackground")},
            inverseJoinColumns={@JoinColumn(name="SKILL_ID", referencedColumnName="idSkill")})
    private List<Skill> skills = new ArrayList<>();

    @OneToOne(mappedBy = "background",fetch = FetchType.LAZY)
    private Candidate candidate;

    @OneToOne(mappedBy = "background",fetch = FetchType.LAZY)
    private Employee employee;

    @OneToOne(mappedBy = "background",fetch = FetchType.LAZY)
    private Vacancy vacancy;

}
