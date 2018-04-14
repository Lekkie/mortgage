package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class IllegalMortgageArgumentException extends LoanRuntimeException {

    public IllegalMortgageArgumentException(String message){
        super(1001, message);
    }

}
