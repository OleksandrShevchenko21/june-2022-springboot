package ua.com.owu.june2022springboot.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.models.dto.CustomerDTO;
import ua.com.owu.june2022springboot.services.CustomerService;

import java.util.List;
@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private CustomerDAO customerDAO;
    private CustomerService customerService;

    //------------------Get All Customers------------------
    @GetMapping("")
    public ResponseEntity<List<Customer>>getCustomers(){
        List<Customer> all = customerDAO.findAll();

        return new ResponseEntity<>(all , HttpStatusCode.valueOf(200));
    }


//------------------Save new Customer------------------
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCustomer(@RequestBody Customer customer){
//        customerDAO.save(customer);
        customerService.save(customer);

    }

    //------------------Get Customer By id------------------
    @GetMapping("/{id}")
    public  ResponseEntity<Customer> getOneCustomer(@PathVariable int id){
        Customer customer = customerDAO.findById(id).get();
        return new ResponseEntity<>(customer,HttpStatusCode.valueOf(200));
    }
    //------------------Get Customer By Name------------------
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Customer>>getCustomersByName(@PathVariable String name){

        return customerService.customerListByName(name);
    }
    //------------------Get Customer By Surname------------------

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Customer>>getCustomersBySurname(@PathVariable String surname){
        return customerService.customerListBySurname(surname);
    }

    //------------------Delete Customer By id------------------
    @DeleteMapping ("/{id}")
    public  void deleteOneCustomer(@PathVariable int id){
        customerDAO.deleteById(id);
    }
    //------------------Update Customer By id------------------
    @PatchMapping("/{id}")
    public void updateCustomer(@PathVariable int id,@RequestBody CustomerDTO customerDTO){
        Customer customer = customerDAO.findById(id).get();
        customer.setName(customerDTO.getUserName());
        customer.setSurname(customerDTO.getUserSurname());
        customer.setEmail(customerDTO.getUserEmail());
        customerDAO.save(customer);

    }

}
