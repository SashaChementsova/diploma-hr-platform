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
public class Hr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHr;
    @OneToOne(fetch = FetchType.LAZY)
    private UserDetail userDetail;

    @OneToMany(mappedBy = "hr")
    private List<Vacancy> vacancyEntities;
}
