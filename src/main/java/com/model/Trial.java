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
public class Trial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTrial;
    @Column
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Vacancy vacancy;

    @OneToMany(mappedBy = "trial")
    private List<Interview> interviewEntities;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ResultTesting resultTesting;

}
