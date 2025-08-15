package com.sinse.xmlapp.exception;

public class BoardException extends RuntimeException{

    public BoardException(String message){
        super(message);
    }
    public BoardException(String message, Throwable e){
        super(message, e);
    }
    public BoardException(Throwable e){
        super(e);
    }

}
