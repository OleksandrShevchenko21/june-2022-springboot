package ua.com.owu.june2022springboot.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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

    @Override
    /*ctrl-o. Будем получать частичку объекта и аутентифицировать его. Т.е принять login, password  и найти объект в базе данных*/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {// это интерфейс
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //username - это всегда логин
                System.out.println(username);
                Customer customer = customerDAO.findCustomerByLogin(username);//создаем метод findCustomerByLogin с помощью JPA и ищем по логину свой объект

                List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority(customer.getRole()));
                //создали обвертку и в нее положили логин, пароль, роль
                User user = new User(
                        customer.getLogin(),
                        customer.getPassword(),
                        roles
                );

//                //создаем интерфейс
//                UserDetails userDetails = new UserDetails() {
//                    @Override
//                    public Collection<? extends GrantedAuthority> getAuthorities() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getPassword() {
//                        return null;
//                    }
//
//                    @Override
//                    public String getUsername() {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isAccountNonExpired() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isAccountNonLocked() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isCredentialsNonExpired() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isEnabled() {
//                        return false;
//                    }
//                };
                return user;
            }
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
                .and() //для чейнинг, чтоб не разрывать ничего
                .httpBasic()//включить базвое http аутентификацию позволяет конвертировать объект, который приходит из "/secure" в объект типа HttpSecurity. Принцип кодирование через формат b64
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//система будет без сессий .Сессии для REST API это плохо


    }
}
