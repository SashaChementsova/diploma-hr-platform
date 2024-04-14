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
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDepartment;
    @Column(nullable = false, unique = true)
    private String nameDepartment;
    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
    private List<PositionName> positionNameEntities;
    public Department(String nameDepartment){
        this.nameDepartment=nameDepartment;
    }

    public void addPositionName(PositionName positionName){
        positionNameEntities.add(positionName);
    }
}
