package com.project.sokdak2.api.exception;

public abstract class GeneralException extends RuntimeException{

    public GeneralException(String message){
        super(message);
    }

    public abstract int getStatusCode();
}
