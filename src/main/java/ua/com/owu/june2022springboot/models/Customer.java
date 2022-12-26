package ua.com.owu.june2022springboot.models;


import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @Column(unique = true) //логин должен быть уникальный; unique = true - база не будет записывать не уникальные значения
    private String login;

    private String password;

    private String role = "CLIENT";
}
