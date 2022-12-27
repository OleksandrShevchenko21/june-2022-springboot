package ua.com.owu.june2022springboot.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration // нужен чтоб создавать Bean
@EnableWebSecurity //внедряет дефолтные настройки чтоб SecurityConfig начинало обработку запросов
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {      //Spring 2.6.7

    private CustomerDAO customerDAO;


    /*создаем дополнительный объект, чтоб сопоставить зашифрованный пароль с расшифрованным*/
    @Bean //, аннотация, которая позволяет с того, что возвращается с нашего метода сделать объект и положиьт его в Bean Conteiner
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    /*ctrl-o. Будем получать частичку объекта и аутентифицировать его. Т.е принять login, password  и найти объект в базе данных*/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // это интерфейс
        auth.userDetailsService(username -> { //username - это всегда логин
//            System.out.println(username);
            Customer customer = customerDAO.findCustomerByLogin(username);//создаем метод findCustomerByLogin с помощью JPA и ищем по логину свой объект

          /*  List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority(customer.getRole())); //обвертка для стринговых репрезентаций ролей
            //создали обвертку и в нее положили логин, пароль и список ролей
            User user = new User(
                    customer.getLogin(),
                    customer.getPassword(),
                    roles   //коллекция объектов GrantedAuthority  */

            /* берет этот объект и идет в базу данных.В базе данных находит
            объект с нашим именем. Проверит его логин, пароль и его роли
            сопоставит с частью .antMatchers(HttpMethod.GET,"/secure").hasAnyRole("ADMIN","CLIENT"),
            чтоб можно было перейти на ту или другую URL, определяется тем какой список ролей у тебя сейчас есть */
            return new User(
                    customer.getLogin(),
                    customer.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(customer.getRole()))   //коллекция объектов GrantedAuthority
            );
        });

    }

    @Override
/*конфигурировать HTTP requests, т.е. из разрешения(access) и запреты(denied) */
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // это подход MVC Model View Controller он устаревший. Мы сним не работаем. Все работают с API потому что потом их можно импортировать на мобильные приложения
                .cors().disable()  //
                .authorizeRequests() //начинается процесс авторизации. Он включает:
                .antMatchers(HttpMethod.GET,"/","/open").permitAll() //обрабатываем URL методом GET, которая будет выглядеть как "/"("/open" - дополнительная). И она доступна всем
                .antMatchers(HttpMethod.POST,"/save").permitAll()
                .antMatchers(HttpMethod.GET,"/secure").hasAnyRole("ADMIN","CLIENT")// доступна тем у кого есть ROLE
//                .antMatchers("/img/**").permitAll() //иначе image не будут отображаться, потому что img и его src это запрос
//                .antMatchers("/**.css").permitAll() //
                .and() //для чейнинг, чтоб не разрывать ничего
                .httpBasic()//включить базвое http аутентификацию позволяет конвертировать объект, который приходит из "/secure" в объект типа HttpSecurity. Принцип кодирование через формат b64
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//система будет без сессий .Сессии для REST API это плохо


    }
}
