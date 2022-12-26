package ua.com.owu.june2022springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.List;

// dao. Создаем конфиурацию объекта (прослойку) dao, которая будет сохранять объекты в базу данных
public interface CustomerDAO extends JpaRepository<Customer,Integer> { // JpaRepository под капотом Спринга уже имеет необходимые реализации всех
                                                                        // crud оперций, которые мне необходимы. Ему надо сказать с
                                                                        // каким типом данных(т.е. с какой моделью (Customer)) ему
                                                                        //  работать и что в этой моделе является идентификатором(Integer)

//    @Query("select c from Customer c where c.name =:name")
//    List<Customer> getByName(@Param("name")String name);

    List<Customer>findCustomerByName(String name); //Spring Data JPA
    List<Customer>findCustomerBySurname(String surname);
}
