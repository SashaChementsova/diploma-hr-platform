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
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    private LevelPosition levelPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    private PositionName positionName;

    @OneToMany(mappedBy = "position")
    private List<Employee> employeeEntities;

    @OneToMany(mappedBy = "position")
    private List<Vacancy> vacancyEntities;

    @OneToMany(mappedBy = "position")
    private List<PositionTestQuestion> positionTestQuestionEntities;

    @Override
    public String toString() {
        return positionName.getName()+" "+levelPosition.getLevel();
    }
}
