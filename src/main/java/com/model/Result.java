package com.model;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idResult;
    //@Column(nullable = false)
    private float points;
    //@Column(nullable = false)
    private String feedback;

    @OneToOne(mappedBy = "result",fetch = FetchType.LAZY)
    private Interview interview;
    @OneToOne(mappedBy = "result",fetch = FetchType.LAZY)
    private PositionTest positionTest;

    @OneToOne(mappedBy = "result",fetch = FetchType.LAZY)
    private LanguageTest languageTest;
}
