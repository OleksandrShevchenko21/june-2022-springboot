package ua.com.owu.june2022springboot.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ua.com.owu.june2022springboot.models.views.Views;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //создаем сущность

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Admin.class})
    private int id;
    @NotEmpty// валидация (см.ErrorController)
    @Length(min = 3, max=12, message = "achtung name")// валидация (см.ErrorController)
    @JsonView({Views.Admin.class,Views.Client.class}) /* если у нас на
                                                        методе контороллера будет значиться, что мы делаем какой-то реквест
                                                        со стороны вроде как админа и клиента, то мы должны показать это свойство*/
    private String name;
    @JsonView({Views.Admin.class,Views.Client.class})
    private String surname;
    @JsonView({Views.Admin.class})  /* если у нас на
                                                        методе контороллера будет значиться, что мы делаем какой-то реквест
                                                        со стороны вроде как ТОЛЬКО АДМИНА, то мы должны показать это свойство*/
    private boolean isActivated =false;
    @JsonView({Views.Admin.class})
    private String email;
    @JsonView({Views.Admin.class,Views.Client.class})
    private String avatar;



    public Customer(String name, String surname, String email, String avatar) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.avatar = avatar;
    }
}
