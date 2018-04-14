package com.landbay.mortgage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.landbay.mortgage.controller.InvestmentController;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lekanomotayo on 11/04/2018.
 */
public class InvestmentRequest {

    private Long lenderId;
    private Long loanId;
    private BigDecimal amount;
    private int termInMonths;


    public InvestmentRequest(){

    }

    public InvestmentRequest(Builder builder){
        this.lenderId = builder.lenderId;
        this.loanId = builder.loanId;
        this.amount = builder.amount;
        this.termInMonths = builder.termInMonths;
    }



    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
    }

    public void validateRequest(){

        if(lenderId == null || lenderId == 0)throw new IllegalMortgageArgumentException("Missing lender");
        if(loanId == null || loanId == 0)throw new IllegalMortgageArgumentException("Missing loan");
        if(amount == null || amount.doubleValue() == 0)throw new IllegalMortgageArgumentException("Missing investment amount");
        if(termInMonths < 1)throw new IllegalMortgageArgumentException("Investment term must be at least 1 month");
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }



    public static class Builder{

        private Long lenderId;
        private Long loanId;
        private BigDecimal amount;
        private int termInMonths;


        public Builder lenderId(Long val){
            lenderId = val;
            return this;
        }
        public Builder loanId(Long val){
            loanId = val;
            return this;
        }
        public Builder amount(BigDecimal val){
            amount = val;
            return this;
        }
        public Builder termInMonths(int val){
            termInMonths = val;
            return this;
        }

        public InvestmentRequest build(){
            return new InvestmentRequest(this);
        }
    }

}
