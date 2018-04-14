package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class LoanApplicationFailureException extends LoanRuntimeException {

    public LoanApplicationFailureException(String message){
        super(1001, "Loan Application failure: " + message);
    }

}
