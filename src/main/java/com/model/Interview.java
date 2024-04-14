package com.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInterview;
    @Column(nullable = false)
    private Date dateAndTime;
    @Column(nullable = false)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trial trial;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @OneToOne( fetch = FetchType.LAZY)
    private Result result;

}
