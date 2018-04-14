package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class LoanRuntimeException extends RuntimeException {

    protected final int errorCode;
    protected final String message;

    public LoanRuntimeException(int errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }


    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
