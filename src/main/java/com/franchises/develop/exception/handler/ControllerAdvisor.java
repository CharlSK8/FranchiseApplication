package com.franchises.develop.exception.handler;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ControllerAdvisor  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ResponseDTO<Object>>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        ResponseDTO<Object> response = ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value())
                .response(errorMessages)
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseDTO<Object>>> handleValidationException(WebExchangeBindException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        ResponseDTO<Object> response = ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value())
                .response(errorMessages)
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ResponseDTO<Object>>> handleValidationException(ResourceNotFoundException ex) {
        ResponseDTO<Object> response = ResponseDTO.builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }



}
