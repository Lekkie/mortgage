package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class IllegalMortgageStateException extends LoanRuntimeException {

    public IllegalMortgageStateException(String message){
        super(1004, message);
    }

}
