package ua.com.owu.june2022springboot.controllers;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomerDAO customerDAO;
    public PasswordEncoder passwordEncoder; //отсюда достается созданный @Bean

    @GetMapping("/")
    public String open(){
        return "open";
    }
    @PostMapping("/save")
    public void save(@RequestBody Customer customer){
        String password = customer.getPassword(); // берем с customer пароль для зашифровки с помощью PasswordEncoder
        String encode = passwordEncoder.encode(password); //бередаем стрингу и он ее кодирует
        customer.setPassword(encode);
        customerDAO.save(customer);
    }
    /*будет работать если сохраним в базу данных соответсвующий объект и у него будет соответсвующая роль*/
    @GetMapping("/secure")
    public String secure(){
        return "secure";
    }

}


