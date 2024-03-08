package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.response.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String error(HttpServletRequest req, Model model){
        String statusCode
                =  String.valueOf(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        String message = "";

        if(statusCode.equals("404")){
            message = "페이지가 존재 하지 않습니다.";
        } else if(statusCode.startsWith("4")){
            message = "잘못된 요청 입니다.";
        } else {
            message = "요청 중 오류가 발생하였습니다.";
        }

        ErrorResponse response
                = ErrorResponse.builder()
                .code(statusCode)
                .message(message)
                .build();

        model.addAttribute("error", response);
        return "/errors/error";
    }
}
