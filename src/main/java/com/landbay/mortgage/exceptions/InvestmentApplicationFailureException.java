package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class InvestmentApplicationFailureException extends LoanRuntimeException {

    public InvestmentApplicationFailureException(String message){
        super(1005, "Loan Application failure: " + message);
    }

}
