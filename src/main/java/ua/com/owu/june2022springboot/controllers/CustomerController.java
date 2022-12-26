package ua.com.owu.june2022springboot.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.models.dto.CustomerDTO;
import ua.com.owu.june2022springboot.models.views.Views;
import ua.com.owu.june2022springboot.services.CustomerService;

import java.io.File;
import java.io.IOException;
import java.util.List;
@RequestMapping("/customers") //все его методы реагируют на запрос "/customers"
@AllArgsConstructor

//создаем класс, экземпляр которого будет создан самим Спрингом. В этом классе будут находиться методы, которые смогут реагировать на тот или другой запрос як админ пользователя
@RestController   //создает new CustomerController(). Создает какой-то объект и положит его в ячейку памяти
public class CustomerController {

    private CustomerDAO customerDAO; /* чтоб работать с базой данной надо содать это поле
                                    CustomerDAO customerDAO - это зависиомость для CustomerController мы внедреям ее
                                    и Спринг самостоятельно находит реализацию
                                    Его обязательно положить в конструктор и это решает @AllArgsConstructor */
    private CustomerService customerService;// используем CustomerService customerService, т.к он делает рутину, проверки и т.д

    //------------------Get All Customers------------------
    @GetMapping("") // метод работает на эту URL в кавычках. Делаем ассоциацию на этот запрос c помошью GetMapping. Количество URL может быть несколько через запятую
    @JsonView({Views.Client.class}) // представление будет формироваться для клиента
    public ResponseEntity<List<Customer>>getCustomers(){      //ResponseEntity т.е. сущность для ответа и будет параметризовано под объекты, которые мы возвращаем(список customer)
        List<Customer> all = customerDAO.findAll(); //мы по факту обращаемся к customerDAO. , где существует все необходимые методы

        return new ResponseEntity<>(all , HttpStatusCode.valueOf(200));
    }


//------------------Save new Customer------------------
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCustomer(@RequestBody Customer customer){  //  принимает отправленного customer с @RequestBody
        customerService.save(customer);
    }
    @PostMapping("/saveWithAvatar")
    public void saveWithAvatar(@RequestParam String name, // @RequestParam - берем информацию с формы
                               @RequestParam String surname,
                               @RequestParam String email, @RequestParam MultipartFile avatar) throws IOException {
        Customer customer = new Customer(name, surname, email, avatar.getOriginalFilename());
        customerService.save(customer);
        String pathname = System.getProperty("user.home")+File.separator + "images" + File.separator + avatar.getOriginalFilename();
        avatar.transferTo(new File(pathname));// переносим картинку

    }


    //------------------Get Customer By id------------------
    @GetMapping("/{id}")                                                    // реагирует на идентифактор id
    @JsonView({Views.Admin.class})                                              // представление будет формироваться для админа
    public  ResponseEntity<Customer> getOneCustomer(@PathVariable int id){      // идентификато и @PathVariable {"id"} int id должны совпадать
        Customer customer = customerDAO.findById(id).get();                 //возвращает объект по id типа optional соотвественно говорим ему get
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
    public void updateCustomer(@PathVariable int id,@RequestBody CustomerDTO customerDTO){  // берем инфо из DTO
        Customer customer = customerDAO.findById(id).get();                             // нашли объект с базы данных
        customer.setName(customerDTO.getUserName());                                        // назначим имя, которое приняли от клиента customerDTO.
        customer.setSurname(customerDTO.getUserSurname());                                  // назначим surname, которое приняли от клиента customerDTO.
        customer.setEmail(customerDTO.getUserEmail());                                      // назначим email, которое приняли от клиента customerDTO.
        customerDAO.save(customer);                                                    // сохраняем customer, который мы обновили

    }

    @GetMapping("/activate/{id}") // если кликаем на URL это всегда GET запросы
    public void activateCustomer(@PathVariable int id){
        Customer customer = customerService.getCustomerById(id);
        customer.setActivated(true);
        customerService.updateCustomer(customer);
            }

}
