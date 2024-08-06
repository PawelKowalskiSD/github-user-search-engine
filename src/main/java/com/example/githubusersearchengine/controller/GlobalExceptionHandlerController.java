package com.example.githubusersearchengine.controller;

import com.example.githubusersearchengine.controller.dto.GithubUserHandleMessageDto;
import com.example.githubusersearchengine.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GithubUserHandleMessageDto> handleUserNotFoundException(UserNotFoundException exception) {
        GithubUserHandleMessageDto githubUserHandleMessageDto = new GithubUserHandleMessageDto(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(githubUserHandleMessageDto);
    }
}