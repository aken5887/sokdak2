package com.project.sokdak2.api.exception;

public class UserNotFoundException extends CommonException{
    private static final String MESSAGE = "유저를 찾을 수 없습니다.";

    public UserNotFoundException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
