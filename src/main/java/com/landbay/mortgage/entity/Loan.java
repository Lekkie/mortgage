package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lekanomotayo on 10/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_loans")
@Cacheable(true)
@SuppressWarnings("serial")
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long loanId;
    @Column(name = "borrower_id", nullable = false, unique = false)
    private Long borrowerId;
    @Column(name = "property_id", nullable = false, unique = false)
    private Long propertyId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "purpose", nullable = false, unique = false)
    private String purpose;
    @Column(name = "loan_amount", nullable = false)
    private BigDecimal loanAmount;
    @Column(name = "down_payment", nullable = false)
    private BigDecimal downPayment;
    @Column(name = "borrower_rate", nullable = false)
    private BigDecimal borrowerRate;
    @Column(name = "lender_rate", nullable = false)
    private BigDecimal lenderRate;
    @Column(name = "ltv", nullable = false)
    private double ltv;
    @Column(name = "tenure_in_months", nullable = false)
    private int tenureInMonths;
    @Column(name = "completion_date", nullable = false)
    private Date completionDate;
    @Column(name = "investment_open", nullable = false)
    private boolean investmentOpen;

    public Loan(){

    }

    public Loan(Builder builder){
        this.loanId = builder.loanId;
        this.borrowerId = builder.borrowerId;
        this.propertyId = builder.propertyId;
        this.productId = builder.productId;
        this.purpose = builder.purpose;
        this.loanAmount = builder.loanAmount;
        this.downPayment = builder.downPayment;
        this.borrowerRate = builder.borrowerRate;
        this.lenderRate = builder.lenderRate;
        this.ltv = builder.ltv;
        this.tenureInMonths = builder.tenureInMonths;
        this.completionDate = builder.completionDate;
        this.investmentOpen = builder.investmentOpen;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getBorrowerRate() {
        return borrowerRate;
    }

    public void setBorrowerRate(BigDecimal borrowerRate) {
        this.borrowerRate = borrowerRate;
    }

    public BigDecimal getLenderRate() {
        return lenderRate;
    }

    public void setLenderRate(BigDecimal lenderRate) {
        this.lenderRate = lenderRate;
    }

    public double getLtv() {
        return ltv;
    }

    public void setLtv(double ltv) {
        this.ltv = ltv;
    }

    public int getTenureInMonths() {
        return tenureInMonths;
    }

    public void setTenureInMonths(int tenureInMonths) {
        this.tenureInMonths = tenureInMonths;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public boolean isInvestmentOpen() {
        return investmentOpen;
    }

    public void setInvestmentOpen(boolean investmentOpen) {
        this.investmentOpen = investmentOpen;
    }


    public static class Builder{

        private Long loanId;
        private Long borrowerId;
        private Long propertyId;
        private Long productId;
        private String purpose;
        private BigDecimal loanAmount;
        private BigDecimal downPayment;
        private BigDecimal borrowerRate;
        private BigDecimal lenderRate;
        private double ltv;
        private int tenureInMonths;
        private Date completionDate;
        private boolean investmentOpen;
        private String createdBy;
        private Date createdOn;


        public Builder loanId(Long val){
            loanId = val;
            return this;
        }
        public Builder borrowerId(Long val){
            borrowerId = val;
            return this;
        }
        public Builder propertyId(Long val){
            propertyId = val;
            return this;
        }
        public Builder productId(Long val){
            productId = val;
            return this;
        }
        public Builder purpose(String val){
            purpose = val;
            return this;
        }
        public Builder loanAmount(BigDecimal val){
            loanAmount = val;
            return this;
        }
        public Builder downPayment(BigDecimal val){
            downPayment = val;
            return this;
        }
        public Builder borrowerRate(BigDecimal val){
            borrowerRate = val;
            return this;
        }
        public Builder lenderRate(BigDecimal val){
            lenderRate = val;
            return this;
        }
        public Builder ltv(double val){
            ltv = val;
            return this;
        }
        public Builder tenureInMonths(int val){
            tenureInMonths = val;
            return this;
        }
        public Builder completionDate(Date val){
            completionDate = val;
            return this;
        }
        public Builder investmentOpen(boolean val){
            investmentOpen = val;
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

        public Loan build(){
            return new Loan(this);
        }
    }

}
