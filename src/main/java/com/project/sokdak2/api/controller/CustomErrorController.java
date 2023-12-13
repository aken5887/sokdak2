package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.response.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String error(HttpServletRequest req, Model model){
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String code = "";
        String message = "오류가 발생하였습니다.";

        if(statusCode != null){
            if(String.valueOf(statusCode).startsWith("4")){
                code = "4xx";
                message = "잘못된 요청 입니다.";
            }else{
                code = "5xx";
                message = "요청 중 오류가 발생하였습니다.";
            }
        }
        ErrorResponse response
                = ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();

        model.addAttribute("error", response);
        return "/errors/error";
    }
}
