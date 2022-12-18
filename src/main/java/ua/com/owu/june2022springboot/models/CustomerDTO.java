package ua.com.owu.june2022springboot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
    private String userName;
    private String userSurname;
    private String userEmail;
}
