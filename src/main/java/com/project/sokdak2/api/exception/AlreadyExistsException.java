package com.project.sokdak2.api.exception;

/**
 * author         : choi
 * date           : 2024-04-02
 */
public class AlreadyExistsException extends CommonException{

    private static final String MESSAGE = "지정된 파일을 찾을 수 없습니다.";

    public AlreadyExistsException(){
        super(MESSAGE);
    }
    public AlreadyExistsException(String message){
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
