package ua.com.owu.june2022springboot.controllers;


import lombok.AllArgsConstructor;
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

    @GetMapping("/")
    public String open(){
        return "open";
    }
    @PostMapping("/save")
    public void save(@RequestBody Customer customer){
        customerDAO.save(customer);
    }
    /*будет работать если сохраним в базу данных соответсвующий объект и у него будет соответсвующая роль*/
    @GetMapping("/secure")
    public String secure(){
        return "secure";
    }

}


