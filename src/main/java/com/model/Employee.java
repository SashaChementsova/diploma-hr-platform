package com.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEmployee;

    @Column(nullable = false)
    private Date dateRecruitment;
    @Column(nullable = false)
    private Date dateContractEnd;

    @OneToOne(fetch = FetchType.LAZY)
    private UserDetail userDetail;

    @OneToOne( fetch = FetchType.LAZY)
    private Background background;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="EmployeeHasLanguages",
            joinColumns={@JoinColumn(name="EMPLOYEE_ID", referencedColumnName="idEmployee")},
            inverseJoinColumns={@JoinColumn(name="LANGUAGE_ID", referencedColumnName="idLanguage")})
    private List<Language> languages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private CityCompany cityCompany;

    @OneToMany(mappedBy = "employee")
    private List<Education> educationEntities;

    @OneToMany(mappedBy = "employee")
    private List<Interview> interviewEntities;

    public String getUserSNP(){
        return userDetail.getSNP();
    }


}
