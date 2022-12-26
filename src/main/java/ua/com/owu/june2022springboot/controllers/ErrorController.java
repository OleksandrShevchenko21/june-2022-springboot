package ua.com.owu.june2022springboot.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.owu.june2022springboot.models.dto.ErrorDTO;

/*создаем ErrorController для подхватывания Customer ошибок*/
@RestControllerAdvice // аннотация говорит, что этот контроллер подхватит только ошибки
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class) /*аннотация говорит, что этот метод в этом контроллере
     будет срабатывать в случае если будет ошибка и в данном случае надо
     сказать какя именно ошибка (ConstraintViolationException.class)*/
    public ErrorDTO errorValidation(ConstraintViolationException e){  //чтоб эта ошибка наполнилась информацией инициализируем ее в своем аргументе

        return new ErrorDTO(500,e.getMessage());// в качестве меседже мы должны сделать обращение к нашему exception и сказать ему get.message просто
    }
}
