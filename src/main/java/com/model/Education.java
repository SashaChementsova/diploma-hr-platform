package com.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.text.SimpleDateFormat;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEducation;

    @Column(nullable = false)
    private String nameEducation;

    @Column(nullable = false)
    private int yearOfGraduation;

    @ManyToOne(fetch = FetchType.LAZY)
    private EducationType educationType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    public String getStringYearOfGraduation() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(yearOfGraduation);
        String year=date.substring(0,4);
        return year;
    }

    @Override
    public String toString() {
        return educationType.getTypeOfEducation()+": "+nameEducation+" ("+yearOfGraduation+")";
    }
}
