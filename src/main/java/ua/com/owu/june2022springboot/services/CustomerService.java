package ua.com.owu.june2022springboot.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.List;
/*Сервисы - это прослойки(классы, интерфейсы), над которыми стоит аннотация @Service(она сделает из них Bean).
Эти прослойки делают сервисную работу или бизнес логику*/
@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerDAO customerDAO; // инициализируем CustomerDAO customerDAO, потому что пишем кофигурацию объекта
    private MailService mailService;// инициализируем ailService mailService

    public void save(Customer customer) {
//        customer.setActivationToken(new ActivationToken());
        customerDAO.save(customer);//сохраняем customer
        mailService.send(customer); // отправляем информацию на полученную почту. К этому моменту customer дойдет с идентификатором, потому что  customerDAO.save(customer); его уже сохраняет
    }

    public ResponseEntity<List<Customer>> customerListByName(String name) {
        if (name != null && !name.isBlank()) {
            List<Customer> customerByName = customerDAO.findCustomerByName(name);
            System.out.println(customerByName);
            return new ResponseEntity<>(customerByName, HttpStatusCode.valueOf(200));
        } else {
            throw new RuntimeException();
        }

    }

    public ResponseEntity<List<Customer>> customerListBySurname(String surname) {
        if (surname != null && !surname.isBlank()) {
            List<Customer> customerBySurname = customerDAO.findCustomerBySurname(surname);
            return new ResponseEntity<>(customerBySurname, HttpStatusCode.valueOf(200));
        } else {
            throw new RuntimeException();
        }

    }
    public Customer getCustomerById(int id){  // находит конкретного Customer по его идентификатору
        return customerDAO.findById(id).get();
    }
    public void updateCustomer(Customer customer){
        customerDAO.save(customer);    //если save принимает объект у которого есть идентификатор, то
                                            // он по идентификатору его находит и те поля, которые изменены
                                            // он их обновляет в базе данных
    }
}
