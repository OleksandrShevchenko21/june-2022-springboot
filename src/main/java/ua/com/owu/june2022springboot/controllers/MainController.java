package ua.com.owu.june2022springboot.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.models.dto.CustomerDTO;

import java.lang.annotation.Retention;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomerDAO customerDAO;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @GetMapping("/")
    public String open(){
        return "open";
    }
    @PostMapping("/save")
    public void save(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setLogin(customerDTO.getName());
        customerDAO.save(customer);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody CustomerDTO customerDTO){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerDTO.getName(), customerDTO.getPassword()));

        if(authenticate != null){
            /*формируем токен*/
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "okten".getBytes(StandardCharsets.UTF_8))
                    .compact();
            System.out.println(jwtToken);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " +jwtToken);
            return new ResponseEntity<>("you are log in", headers, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("zazazaz",HttpStatus.FORBIDDEN);
    }

    @GetMapping("/secure")
    public String secure (){
        return "secure data";
    }

}
