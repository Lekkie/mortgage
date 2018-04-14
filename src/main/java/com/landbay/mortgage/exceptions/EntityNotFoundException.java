package com.landbay.mortgage.exceptions;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class EntityNotFoundException extends LoanRuntimeException {

    public EntityNotFoundException(String entity){
        super(1002, entity + " not found");
    }

}
