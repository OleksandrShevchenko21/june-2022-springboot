package ua.com.owu.june2022springboot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private boolean isActivated =false;
    private String email;
    private String avatar;

    public Customer(String name, String surname, String email, String avatar) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.avatar = avatar;
    }
}
