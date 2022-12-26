package ua.com.owu.june2022springboot.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/* DTO - паттерн (Data Transfer Object)
* Прослойка, в которую мы будем принимать информацию от клиента
*  и с которого будем передавать информацию в нашу базу данных или объект,
* который  работает с нашей базой данной*/
public class CustomerDTO {
    private String userName;
    private String userSurname;
    private String userEmail;
}
