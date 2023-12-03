package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.exception.CommonException;
import com.project.sokdak2.api.exception.PageNotFoundException;
import com.project.sokdak2.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({PageNotFoundException.class})
    public String notFound(PageNotFoundException e, Model model) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        model.addAttribute("error", errorResponse);

        return "/errors/error";
    }

    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> commonException(CommonException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response
                = ResponseEntity
                    .status(e.getStatusCode())
                    .body(errorResponse);

        return response;
    }
}
