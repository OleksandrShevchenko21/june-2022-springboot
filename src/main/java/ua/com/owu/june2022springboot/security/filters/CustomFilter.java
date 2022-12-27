package ua.com.owu.june2022springboot.security.filters;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomFilter extends OncePerRequestFilter { //встроенный механизм, который сработает один раз за реквест

    //ctrl+I
    /* в этом фильтре подхватываем те запросы, которые прийдут со стороны пользователя с токеном */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

    }
}
