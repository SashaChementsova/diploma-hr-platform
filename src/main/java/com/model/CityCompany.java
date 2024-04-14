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
public class CityCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCityCompany;

    @Column(nullable=false, unique=true)
    private String nameCity;

    @OneToMany(mappedBy = "cityCompany")
    private List<Employee> employeeEntities;

    @OneToMany(mappedBy = "cityCompany")
    private List<Vacancy> vacancyEntities;

    public CityCompany(String nameCity) {
        this.nameCity = nameCity;
    }
}
