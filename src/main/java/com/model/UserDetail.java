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
public class UserDetail implements Comparable<UserDetail>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserDetails;
    @Column(nullable=false)
    private String name;
    private String patronymic;
    @Column(nullable=false)
    private String surname;
    @Column(nullable=false, unique=true)
    private String phone;
    @Column(nullable=false)
    private Date birthday;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    private String info;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="UserDetailsHasRole",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="idUserDetails")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="idRole")})
    private List<Role> roles;

    @OneToOne(mappedBy = "userDetail", fetch = FetchType.EAGER)
    private Admin admin;

    @OneToOne(mappedBy = "userDetail", fetch = FetchType.EAGER)
    private Hr hr;


    @OneToOne(mappedBy = "userDetail", fetch = FetchType.EAGER)
    private Candidate candidate;

    @OneToOne(mappedBy = "userDetail", fetch = FetchType.LAZY)
    private Employee employee;

    @OneToMany(mappedBy = "userDetail")
    private List<UserDetailsHasChats> userDetailsHasChatsEntities;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    public void addRole(Role role){
        roles.add(role);
    }

    public String getSNP(){
        if(patronymic.equals("-")){
            return surname+" "+name;
        }
        return surname+" "+name+" "+patronymic;
    }
    public String getShortSNP(){
        if(patronymic.equals("-")){
            return surname+" "+name.charAt(0)+".";
        }
        return surname+" "+name.charAt(0)+"."+patronymic.charAt(0)+".";
    }

    @Override
    public int compareTo(UserDetail u) {
        return getSNP().compareTo(u.getSNP());
    }

}
