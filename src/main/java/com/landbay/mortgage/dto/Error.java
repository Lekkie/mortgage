package com.landbay.mortgage.dto;

/**
 * Created by lekanomotayo on 11/04/2018.
 */
public final class Error{

    private final int code;
    private final String message;

    public Error(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
