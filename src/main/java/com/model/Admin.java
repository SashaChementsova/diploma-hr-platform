package com.model;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAdmin;
    @Column(nullable = false,unique = true)
    private String nameCompany;
    @Column(nullable = false,unique = true)
    private String nameDirector;

    @OneToOne(fetch = FetchType.LAZY)     ///////////////////////////////////////////////////////////////
    private UserDetail userDetail;
}
