package com.landbay.mortgage.dto;

import com.landbay.mortgage.entity.LoanInvestment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 11/04/2018.
 */
public final class LoanInfo{

    private final Long loanId;
    private final Long borrowerId;
    private final String postcode;
    private final String county;
    private final Integer beds;
    private final Double propertyValue;
    private final Double monthlyRent;
    private final String applicationType;
    private final String product;
    private final String purpose;
    private final Double loanAmount;
    private final Double borrowerRate;
    private final Double lenderRate;
    private final Double ltv;
    private final Integer tenureInMonths;
    private final String completionDate;
    private final Double rentalCoverage;
    private final String productType;
    private final String propertyType;
    private final String borrowerType;
    private final Double monthlyRepayment;
    private final List<LoanInvestment> investments;

    public LoanInfo(Builder builder){
        this.loanId = builder.loanId;
        this.borrowerId = builder.borrowerId;
        this.postcode = builder.postcode;
        this.county = builder.county;
        this.beds = builder.beds;
        this.propertyValue = builder.propertyValue;
        this.monthlyRent = builder.monthlyRent;
        this.applicationType = builder.applicationType;
        this.product = builder.product;
        this.purpose = builder.purpose;
        this.loanAmount = builder.loanAmount;
        this.borrowerRate = builder.borrowerRate;
        this.lenderRate = builder.lenderRate;
        this.ltv = builder.ltv;
        this.tenureInMonths = builder.tenureInMonths;
        this.completionDate = builder.completionDate;
        this.rentalCoverage = builder.rentalCoverage;
        this.productType = builder.productType;
        this.propertyType = builder.propertyType;
        this.borrowerType = builder.borrowerType;
        this.monthlyRepayment = builder.monthlyRepayment;
        this.investments = builder.investments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCounty() {
        return county;
    }

    public Integer getBeds() {
        return beds;
    }

    public Double getPropertyValue() {
        return propertyValue;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getProduct() {
        return product;
    }

    public String getPurpose() {
        return purpose;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public Double getBorrowerRate() {
        return borrowerRate;
    }

    public Double getLenderRate() {
        return lenderRate;
    }

    public Double getLtv() {
        return ltv;
    }

    public Integer getTenureInMonths() {
        return tenureInMonths;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public Double getRentalCoverage() {
        return rentalCoverage;
    }

    public String getProductType() {
        return productType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public Double getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public List<LoanInvestment> getInvestments() {
        return investments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanInfo loanInfo = (LoanInfo) o;

        if (loanId != null ? !loanId.equals(loanInfo.loanId) : loanInfo.loanId != null) return false;
        if (borrowerId != null ? !borrowerId.equals(loanInfo.borrowerId) : loanInfo.borrowerId != null) return false;
        if (postcode != null ? !postcode.equals(loanInfo.postcode) : loanInfo.postcode != null) return false;
        if (county != null ? !county.equals(loanInfo.county) : loanInfo.county != null) return false;
        if (beds != null ? !beds.equals(loanInfo.beds) : loanInfo.beds != null) return false;
        if (propertyValue != null ? !propertyValue.equals(loanInfo.propertyValue) : loanInfo.propertyValue != null) return false;
        if (monthlyRent != null ? !monthlyRent.equals(loanInfo.monthlyRent) : loanInfo.monthlyRent != null) return false;
        if (applicationType != null ? !applicationType.equals(loanInfo.applicationType) : loanInfo.applicationType != null) return false;
        if (purpose != null ? !purpose.equals(loanInfo.purpose) : loanInfo.purpose != null) return false;
        if (loanAmount != null ? !loanAmount.equals(loanInfo.loanAmount) : loanInfo.loanAmount != null) return false;
        if (borrowerRate != null ? !borrowerRate.equals(loanInfo.borrowerRate) : loanInfo.borrowerRate != null) return false;
        if (lenderRate != null ? !lenderRate.equals(loanInfo.lenderRate) : loanInfo.lenderRate != null) return false;
        if (ltv != null ? !ltv.equals(loanInfo.ltv) : loanInfo.ltv != null) return false;
        if (tenureInMonths != null ? !tenureInMonths.equals(loanInfo.tenureInMonths) : loanInfo.tenureInMonths != null) return false;
        if (completionDate != null ? !completionDate.equals(loanInfo.completionDate) : loanInfo.completionDate != null) return false;
        if (rentalCoverage != null ? !rentalCoverage.equals(loanInfo.rentalCoverage) : loanInfo.rentalCoverage != null) return false;
        if (productType != null ? !productType.equals(loanInfo.productType) : loanInfo.productType != null) return false;
        if (propertyType != null ? !propertyType.equals(loanInfo.propertyType) : loanInfo.propertyType != null) return false;
        if (borrowerType != null ? !borrowerType.equals(loanInfo.borrowerType) : loanInfo.borrowerType != null) return false;
        if (investments != null ? !investments.equals(loanInfo.investments) : loanInfo.investments != null) return false;

        return monthlyRepayment != null ? monthlyRepayment.equals(loanInfo.monthlyRepayment) : loanInfo.monthlyRepayment != null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (loanId != null ? loanId.hashCode() : 0);
        result = 31 * result + (borrowerId != null ? borrowerId.hashCode() : 0);
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (beds != null ? beds.hashCode() : 0);
        result = 31 * result + (propertyValue != null ? propertyValue.hashCode() : 0);
        result = 31 * result + (monthlyRent != null ? monthlyRent.hashCode() : 0);
        result = 31 * result + (applicationType != null ? applicationType.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (loanAmount != null ? loanAmount.hashCode() : 0);
        result = 31 * result + (borrowerRate != null ? borrowerRate.hashCode() : 0);
        result = 31 * result + (lenderRate != null ? lenderRate.hashCode() : 0);
        result = 31 * result + (ltv != null ? ltv.hashCode() : 0);
        result = 31 * result + (tenureInMonths != null ? tenureInMonths.hashCode() : 0);
        result = 31 * result + (completionDate != null ? completionDate.hashCode() : 0);
        result = 31 * result + (rentalCoverage != null ? rentalCoverage.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (propertyType != null ? propertyType.hashCode() : 0);
        result = 31 * result + (borrowerType != null ? borrowerType.hashCode() : 0);
        result = 31 * result + (monthlyRepayment != null ? monthlyRepayment.hashCode() : 0);

        for(LoanInvestment investment: investments){
            result = 31 * result + (investment != null ? investment.hashCode() : 0);
        }

        return result;
    }


    @Override
    public String toString() {
        return "LoanInfo{" +
                "loanId='" + loanId + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", borrowerRate='" + borrowerRate + '\'' +
                ", ltv='" + ltv + '\'' +
                ", rentalCoverage='" + rentalCoverage + '\'' +
                ", monthlyRepayment='" + monthlyRepayment + '\'' +
                '}';
    }


    public static class Builder{

        private Long loanId;
        private Long borrowerId;
        private String postcode;
        private String county;
        private Integer beds;
        private Double propertyValue;
        private Double monthlyRent;
        private String applicationType;
        private String product;
        private String purpose;
        private Double loanAmount;
        private Double borrowerRate;
        private Double lenderRate;
        private Double ltv;
        private Integer tenureInMonths;
        private String completionDate;
        private Double rentalCoverage;
        private String productType;
        private String propertyType;
        private String borrowerType;
        private Double monthlyRepayment;
        List<LoanInvestment> investments;


        public Builder loanId(Long val){
            loanId = val;
            return this;
        }
        public Builder borrowerId(Long val){
            borrowerId = val;
            return this;
        }
        public Builder postcode(String val){
            postcode = val;
            return this;
        }
        public Builder county(String val){
            county = val;
            return this;
        }
        public Builder beds(Integer val){
            beds = val;
            return this;
        }
        public Builder propertyValue(Double val){
            propertyValue = val;
            return this;
        }
        public Builder monthlyRent(Double val){
            monthlyRent = val;
            return this;
        }
        public Builder applicationType(String val){
            applicationType = val;
            return this;
        }
        public Builder product(String val){
            product = val;
            return this;
        }
        public Builder purpose(String val){
            purpose = val;
            return this;
        }
        public Builder loanAmount(Double val){
            loanAmount = val;
            return this;
        }
        public Builder borrowerRate(Double val){
            borrowerRate = val;
            return this;
        }
        public Builder lenderRate(Double val){
            lenderRate = val;
            return this;
        }
        public Builder ltv(Double val){
            ltv = val;
            return this;
        }
        public Builder tenureInMonths(Integer val){
            tenureInMonths = val;
            return this;
        }
        public Builder completionDate(String val){
            completionDate = val;
            return this;
        }
        public Builder rentalCoverage(Double val){
            rentalCoverage = val;
            return this;
        }
        public Builder productType(String val){
            productType = val;
            return this;
        }
        public Builder propertyType(String val){
            propertyType = val;
            return this;
        }
        public Builder borrowerType(String val){
            borrowerType = val;
            return this;
        }
        public Builder monthlyRepayment(Double val){
            monthlyRepayment = val;
            return this;
        }
        public Builder investments(List<LoanInvestment> val){
            investments = val;
            return this;
        }

        public LoanInfo build(){
            return new LoanInfo(this);
        }
    }
}
