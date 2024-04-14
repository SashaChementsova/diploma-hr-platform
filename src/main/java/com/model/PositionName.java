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
public class PositionName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPositionName;

    @Column(nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy = "positionName")
    private List<Position> positionEntities;
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    public PositionName(String name) {
        this.name = name;
    }
    public PositionName(String name,Department department) {
        this.name = name;
        this.department=department;
    }
}
