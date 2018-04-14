package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_loan_investments")
@Cacheable(true)
@SuppressWarnings("serial")
public class LoanInvestment extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long loanInvestmentId;
    @Column(name = "loan_id", nullable = false)
    private Long loanId;
    @Column(name = "lender_id", nullable = false)
    private Long lenderId;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "terms_in_months", nullable = false)
    private Integer termsInMonths;

    public LoanInvestment(){

    }

    public LoanInvestment(Builder builder){
        this.loanInvestmentId = builder.loanInvestmentId;
        this.loanId = builder.loanId;
        this.lenderId = builder.lenderId;
        this.amount = builder.amount;
        this.termsInMonths = builder.termsInMonths;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }


    public Long getLoanInvestmentId() {
        return loanInvestmentId;
    }

    public void setLoanInvestmentId(Long loanInvestmentId) {
        this.loanInvestmentId = loanInvestmentId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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

    public Integer getTermsInMonths() {
        return termsInMonths;
    }

    public void setTermsInMonths(Integer termsInMonths) {
        this.termsInMonths = termsInMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanInvestment loanInvestment = (LoanInvestment) o;

        if (loanInvestmentId != null ? !loanInvestmentId.equals(loanInvestment.loanInvestmentId) : loanInvestment.loanInvestmentId != null) return false;
        if (loanId != null ? !loanId.equals(loanInvestment.loanId) : loanInvestment.loanId != null) return false;
        if (lenderId != null ? !lenderId.equals(loanInvestment.lenderId) : loanInvestment.lenderId != null) return false;
        if (amount != null ? !amount.equals(loanInvestment.amount) : loanInvestment.amount != null) return false;

        return termsInMonths != null ? termsInMonths.equals(loanInvestment.termsInMonths) : loanInvestment.termsInMonths != null;
    }

    @Override
    public int hashCode() {
        int result = 18;
        result = 31 * result + (loanInvestmentId != null ? loanInvestmentId.hashCode() : 0);
        result = 31 * result + (loanId != null ? loanId.hashCode() : 0);
        result = 31 * result + (lenderId != null ? lenderId.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (termsInMonths != null ? termsInMonths.hashCode() : 0);

        return result;
    }



    public static class Builder{

        private Long loanInvestmentId;
        private Long loanId;
        private Long lenderId;
        private BigDecimal amount;
        private Integer termsInMonths;
        private String createdBy;
        private Date createdOn;


        public Builder loanInvestmentId(Long val){
            loanInvestmentId = val;
            return this;
        }
        public Builder loanId(Long val){
            loanId = val;
            return this;
        }
        public Builder lenderId(Long val){
            lenderId = val;
            return this;
        }
        public Builder amount(BigDecimal val){
            amount = val;
            return this;
        }
        public Builder termsInMonths(Integer val){
            termsInMonths = val;
            return this;
        }
        public Builder createdBy(String val){
            createdBy = val;
            return this;
        }
        public Builder createdOn(Date val){
            createdOn = val;
            return this;
        }

        public LoanInvestment build(){
            return new LoanInvestment(this);
        }
    }


}
