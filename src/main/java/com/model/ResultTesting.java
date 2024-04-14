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
public class ResultTesting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idResultTesting;
    @OneToOne(fetch = FetchType.LAZY)
    private PositionTest positionTest;
    @OneToMany(mappedBy = "resultTesting")
    private List<LanguageTest> languageTestEntities;
    @OneToOne(mappedBy = "resultTesting", fetch = FetchType.LAZY)
    private Trial trial;
}
