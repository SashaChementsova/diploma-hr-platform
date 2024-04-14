package com.model;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PositionTestHasQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPositionTestHasQuestion;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    private PositionTest positionTest;
    @ManyToOne(fetch = FetchType.LAZY)
    private PositionTestQuestion positionTestQuestion;
}
