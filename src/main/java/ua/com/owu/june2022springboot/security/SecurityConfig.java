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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.security.filters.CustomFilter;


import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration // нужен чтоб создавать Bean
@EnableWebSecurity //внедряет дефолтные настройки чтоб SecurityConfig начинало обработку запросов
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {      //Spring 2.6.7

    private CustomerDAO customerDAO;
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /* конфигурация через AuthenticationManager*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            System.out.println("login trig"); //проверить работает ли логин, потому что он по факту не выполняем, но он происходит
            Customer customer = customerDAO.findCustomerByLogin(username);
            return new User(
                    customer.getLogin(),
                    customer.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(customer.getRole())));
        });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("GET", "/").permitAll()
                .antMatchers(HttpMethod.POST, "/save").permitAll()
                .antMatchers(HttpMethod.GET, "/secure").hasAnyRole("ADMIN", "USER")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().configurationSource(configurationSource())
                /* делаем кастомный фильтр. Фильтр может быть реализован с помощью функционального интерфейса
                 * добавляем свой собственный фильтр Before до того как сработает UsernamePasswordAuthenticationFilter.class */
                .and().addFilterBefore(
//                        (servletRequest, servletResponse, filterChain) -> { //нарушили цепочку
//
//                            System.out.println("custom filter");
//                            filterChain.doFilter(servletRequest,servletResponse); //воостановили цепочку
//
//
//                },
                customFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }
/*собственный фильтр.показал как содавать собственные фильтры
* в filters создаев customFilter класс */
    public CustomFilter customFilter() {
        return  new CustomFilter();
    }

    /*генерируем с каких серверов/хостов еще разрешено обращаться,
     * какие http методы разрешены,
     * какие хедеры дополнительно надо показывать на стороне клиента */
    @Bean
    public CorsConfigurationSource configurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));   // обычно вместо локал хоста URL возможно с каким-то портом
        configuration.setAllowedHeaders(Arrays.asList("*")); //говорим, что все хедеры разрешено отправлять нам, а сервер будет их принимать. Хедер -метаинформация где например могут храниться логин и пароль, параметры поиска
//        configuration.addAllowedHeader("*"); //для одного хедера
        configuration.setAllowedMethods(Arrays.asList( //можем запретить запрос методом GET, POST и т.д . Сейчас прописываем, которые хотим прямо запретить
                HttpMethod.GET.name(),    //метод name, чтоб превратить название httpMethod енумовскую на стрингу
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.HEAD.name()
        ));
        configuration.addExposedHeader("Authorization"); //  хедеры, которые надо представить, потому что их не видно
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //привязываем конфигурации к определенной URL
        source.registerCorsConfiguration("/**", configuration); // на любые url мы эту кофигурацию применяем

        return source;
    }

}
