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
public class PositionTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPositionTest;
    //@Column(nullable = false)
    private Date date;
    @OneToOne(fetch = FetchType.LAZY)
    private Result result;

    @OneToMany(mappedBy = "positionTest")
    private List<PositionTestHasQuestion> positionTestHasQuestionEntities;

    @OneToOne(mappedBy = "positionTest",fetch = FetchType.LAZY)
    private ResultTesting resultTesting;
}
