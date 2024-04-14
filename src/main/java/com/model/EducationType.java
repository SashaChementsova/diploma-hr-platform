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
public class EducationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEducationType;
    @Column(nullable = false, unique = true)
    private String typeOfEducation;

    @OneToMany(mappedBy = "educationType")
    private List<Education> educationEntities;
}
