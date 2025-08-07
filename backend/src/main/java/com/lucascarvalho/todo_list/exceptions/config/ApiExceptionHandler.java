package com.lucascarvalho.todo_list.exceptions.config;

import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public Map<String, String> handleNotFoundException(Exception ex) {
        Map<String, String> map =  new HashMap<>();
        map.put("error", ex.getMessage());
        return map;
    }
}
