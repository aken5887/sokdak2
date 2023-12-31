package com.project.sokdak2.api.exception;

import org.springframework.http.HttpStatus;

public class PageNotFoundException extends GeneralException{
    private static final String MESSAGE = "페이지를 찾을 수 없습니다.";
    public PageNotFoundException(){
        super(MESSAGE);
    }
    
    public int getStatusCode(){
        return HttpStatus.NOT_FOUND.value();
    }
}
