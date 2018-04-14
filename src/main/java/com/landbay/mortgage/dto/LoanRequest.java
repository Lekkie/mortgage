package com.landbay.mortgage.dto;

import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class LoanRequest {

    String borrowerFirstname;
    String borrowerLastname;
    double borrowerSalary;
    String borrowerNationality;
    int borrowerAge;
    boolean borrowerFirstTimeLandlord;
    boolean borrowerFirstTimeBuyer;
    boolean borrowerActiveBankAccount;
    boolean borrowerUkCreditHistory;
    boolean goodCreditInLastYear;
    boolean haveMinimumDeposit;
    boolean satisfyAssessmentThreshold;
    String borrowerTypeCode;
    String productCode;
    String loanPurpose;
    int loanTenureInMonths;
    String propertyPostcode;
    String propertyCounty;
    String propertyTypeCode;
    String propertyTypeName;
    int propertyBeds;
    double propertyValue;
    double propertyMonthlyRent;
    boolean propertyCloseByCommercial;


    public LoanRequest(){

    }

    public LoanRequest(Builder builder){
        this.borrowerFirstname = builder.borrowerFirstname;
        this.borrowerLastname = builder.borrowerLastname;
        this.borrowerSalary = builder.borrowerSalary;
        this.borrowerNationality = builder.borrowerNationality;
        this.borrowerAge = builder.borrowerAge;
        this.borrowerFirstTimeLandlord = builder.borrowerFirstTimeLandlord;
        this.borrowerFirstTimeBuyer = builder.borrowerFirstTimeBuyer;
        this.borrowerActiveBankAccount = builder.borrowerActiveBankAccount;
        this.borrowerUkCreditHistory = builder.borrowerUkCreditHistory;
        this.goodCreditInLastYear = builder.goodCreditInLastYear;
        this.haveMinimumDeposit = builder.haveMinimumDeposit;
        this.satisfyAssessmentThreshold = builder.satisfyAssessmentThreshold;
        this.borrowerTypeCode = builder.borrowerTypeCode;
        this.productCode = builder.productCode;
        this.loanPurpose = builder.loanPurpose;
        this.loanTenureInMonths = builder.loanTenureInMonths;
        this.propertyPostcode = builder.propertyPostcode;
        this.propertyCounty = builder.propertyCounty;
        this.propertyTypeCode = builder.propertyTypeCode;
        this.propertyTypeName = builder.propertyTypeName;
        this.propertyBeds = builder.propertyBeds;
        this.propertyValue = builder.propertyValue;
        this.propertyMonthlyRent = builder.propertyMonthlyRent;
        this.propertyCloseByCommercial = builder.propertyCloseByCommercial;
    }


    public void validateRequest(){
        if(borrowerFirstname == null || borrowerFirstname.isEmpty())throw new IllegalMortgageArgumentException("Missing borrower's first name");
        if(borrowerLastname == null || borrowerLastname.isEmpty())throw new IllegalMortgageArgumentException("Missing borrower's last name");
        if(borrowerTypeCode == null || borrowerTypeCode.isEmpty())throw new IllegalMortgageArgumentException("Missing borrower type's code");
        if(borrowerSalary <= 25000)throw new IllegalMortgageArgumentException("Borrower's salary must be above 25,000GBP to qualify");
        if(borrowerNationality == null || borrowerNationality.isEmpty())throw new IllegalMortgageArgumentException("Missing borrower's nationality");
        if(borrowerAge <= 21)throw new IllegalMortgageArgumentException("Borrower's age must be above 21");
        if(!borrowerUkCreditHistory)throw new IllegalMortgageArgumentException("Borrowers need a UK footprint to qualify");
        if(!goodCreditInLastYear)throw new IllegalMortgageArgumentException("Borrowers need good credit in the last 1 year");
        if(!haveMinimumDeposit)throw new IllegalMortgageArgumentException("Borrowers need to have minimum deposit");
        if(!satisfyAssessmentThreshold)throw new IllegalMortgageArgumentException("Borrowers must satisfy our assessment threshold");

        if(productCode == null || productCode.isEmpty())throw new IllegalMortgageArgumentException("Missing loan product code");
        if(loanPurpose == null || loanPurpose.isEmpty())throw new IllegalMortgageArgumentException("Missing loan purpose");
        // It is a good idea to put the maximum loan tenure in database, so that operation/business team and
        // change it when it wants to support longer tenure
        if(loanTenureInMonths < 1 || loanTenureInMonths > 300)throw new IllegalMortgageArgumentException("Loan tenure must be more than 1 month or less than 25 years (300 months)");

        if(propertyPostcode == null || propertyPostcode.isEmpty())throw new IllegalMortgageArgumentException("Missing property postcode");
        if(propertyCounty == null || propertyCounty.isEmpty())throw new IllegalMortgageArgumentException("Missing property county");
        if(propertyTypeCode == null || propertyTypeCode.isEmpty())throw new IllegalMortgageArgumentException("Missing property type code");
        if(propertyBeds < 1)throw new IllegalMortgageArgumentException("Bedrooms must be greater than 1");
        // It is preferable idea to put the minimum property value in database
        if(propertyValue < 80000)throw new IllegalMortgageArgumentException("Property value must be greater than 80000");
        if(propertyMonthlyRent < 1)throw new IllegalMortgageArgumentException("Monthly rent must be higher than 0");
    }


    public String getBorrowerFirstname() {
        return borrowerFirstname;
    }

    public void setBorrowerFirstname(String borrowerFirstname) {
        this.borrowerFirstname = borrowerFirstname;
    }

    public String getBorrowerLastname() {
        return borrowerLastname;
    }

    public void setBorrowerLastname(String borrowerLastname) {
        this.borrowerLastname = borrowerLastname;
    }

    public double getBorrowerSalary() {
        return borrowerSalary;
    }

    public void setBorrowerSalary(double borrowerSalary) {
        this.borrowerSalary = borrowerSalary;
    }

    public String getBorrowerNationality() {
        return borrowerNationality;
    }

    public void setBorrowerNationality(String borrowerNationality) {
        this.borrowerNationality = borrowerNationality;
    }

    public int getBorrowerAge() {
        return borrowerAge;
    }

    public void setBorrowerAge(int borrowerAge) {
        this.borrowerAge = borrowerAge;
    }

    public boolean isBorrowerFirstTimeLandlord() {
        return borrowerFirstTimeLandlord;
    }

    public void setBorrowerFirstTimeLandlord(boolean borrowerFirstTimeLandlord) {
        this.borrowerFirstTimeLandlord = borrowerFirstTimeLandlord;
    }

    public boolean isBorrowerFirstTimeBuyer() {
        return borrowerFirstTimeBuyer;
    }

    public void setBorrowerFirstTimeBuyer(boolean borrowerFirstTimeBuyer) {
        this.borrowerFirstTimeBuyer = borrowerFirstTimeBuyer;
    }

    public boolean isBorrowerActiveBankAccount() {
        return borrowerActiveBankAccount;
    }

    public void setBorrowerActiveBankAccount(boolean borrowerActiveBankAccount) {
        this.borrowerActiveBankAccount = borrowerActiveBankAccount;
    }

    public boolean isBorrowerUkCreditHistory() {
        return borrowerUkCreditHistory;
    }

    public void setBorrowerUkCreditHistory(boolean borrowerUkCreditHistory) {
        this.borrowerUkCreditHistory = borrowerUkCreditHistory;
    }

    public boolean isGoodCreditInLastYear() {
        return goodCreditInLastYear;
    }

    public void setGoodCreditInLastYear(boolean goodCreditInLastYear) {
        this.goodCreditInLastYear = goodCreditInLastYear;
    }


    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public int getLoanTenureInMonths() {
        return loanTenureInMonths;
    }

    public void setLoanTenureInMonths(int loanTenureInMonths) {
        this.loanTenureInMonths = loanTenureInMonths;
    }

    public String getPropertyPostcode() {
        return propertyPostcode;
    }

    public void setPropertyPostcode(String propertyPostcode) {
        this.propertyPostcode = propertyPostcode;
    }

    public String getPropertyCounty() {
        return propertyCounty;
    }

    public void setPropertyCounty(String propertyCounty) {
        this.propertyCounty = propertyCounty;
    }


    public String getBorrowerTypeCode() {
        return borrowerTypeCode;
    }

    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    public String getPropertyTypeCode() {
        return propertyTypeCode;
    }

    public void setPropertyTypeCode(String propertyTypeCode) {
        this.propertyTypeCode = propertyTypeCode;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public int getPropertyBeds() {
        return propertyBeds;
    }

    public void setPropertyBeds(int propertyBeds) {
        this.propertyBeds = propertyBeds;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public double getPropertyMonthlyRent() {
        return propertyMonthlyRent;
    }

    public void setPropertyMonthlyRent(double propertyMonthlyRent) {
        this.propertyMonthlyRent = propertyMonthlyRent;
    }

    public boolean isHaveMinimumDeposit() {
        return haveMinimumDeposit;
    }

    public void setHaveMinimumDeposit(boolean haveMinimumDeposit) {
        this.haveMinimumDeposit = haveMinimumDeposit;
    }

    public boolean isSatisfyAssessmentThreshold() {
        return satisfyAssessmentThreshold;
    }

    public void setSatisfyAssessmentThreshold(boolean satisfyAssessmentThreshold) {
        this.satisfyAssessmentThreshold = satisfyAssessmentThreshold;
    }

    public boolean isPropertyCloseByCommercial() {
        return propertyCloseByCommercial;
    }

    public void setPropertyCloseByCommercial(boolean propertyCloseByCommercial) {
        this.propertyCloseByCommercial = propertyCloseByCommercial;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public static class Builder{

        String borrowerFirstname;
        String borrowerLastname;
        double borrowerSalary;
        String borrowerNationality;
        int borrowerAge;
        boolean borrowerFirstTimeLandlord;
        boolean borrowerFirstTimeBuyer;
        boolean borrowerActiveBankAccount;
        boolean borrowerUkCreditHistory;
        boolean goodCreditInLastYear;
        boolean haveMinimumDeposit;
        boolean satisfyAssessmentThreshold;
        String borrowerTypeCode;
        String productCode;
        String applicationTypeCode;
        String loanPurpose;
        int loanTenureInMonths;
        String propertyPostcode;
        String propertyCounty;
        String propertyTypeCode;
        String propertyTypeName;
        int propertyBeds;
        double propertyValue;
        double propertyMonthlyRent;
        boolean propertyCloseByCommercial;


        public Builder borrowerFirstname(String val){
            borrowerFirstname = val;
            return this;
        }
        public Builder borrowerLastname(String val){
            borrowerLastname = val;
            return this;
        }
        public Builder borrowerSalary(double val){
            borrowerSalary = val;
            return this;
        }
        public Builder borrowerNationality(String val){
            borrowerNationality = val;
            return this;
        }
        public Builder borrowerAge(int val){
            borrowerAge = val;
            return this;
        }
        public Builder borrowerFirstTimeLandlord(boolean val){
            borrowerFirstTimeLandlord = val;
            return this;
        }
        public Builder borrowerFirstTimeBuyer(boolean val){
            borrowerFirstTimeBuyer = val;
            return this;
        }
        public Builder borrowerActiveBankAccount(boolean val){
            borrowerActiveBankAccount = val;
            return this;
        }
        public Builder borrowerUkCreditHistory(boolean val){
            borrowerUkCreditHistory = val;
            return this;
        }
        public Builder goodCreditInLastYear(boolean val){
            goodCreditInLastYear = val;
            return this;
        }
        public Builder haveMinimumDeposit(boolean val){
            haveMinimumDeposit = val;
            return this;
        }
        public Builder satisfyAssessmentThreshold(boolean val){
            satisfyAssessmentThreshold = val;
            return this;
        }
        public Builder borrowerTypeCode(String val){
            borrowerTypeCode = val;
            return this;
        }
        public Builder productCode(String val){
            productCode = val;
            return this;
        }
        public Builder applicationTypeCode(String val){
            applicationTypeCode = val;
            return this;
        }
        public Builder loanPurpose(String val){
            loanPurpose = val;
            return this;
        }
        public Builder loanTenureInMonths(int val){
            loanTenureInMonths = val;
            return this;
        }
        public Builder propertyPostcode(String val){
            propertyPostcode = val;
            return this;
        }
        public Builder propertyCounty(String val){
            propertyCounty = val;
            return this;
        }
        public Builder propertyTypeCode(String val){
            propertyTypeCode = val;
            return this;
        }
        public Builder propertyTypeName(String val){
            propertyTypeName = val;
            return this;
        }
        public Builder propertyBeds(int val){
            propertyBeds = val;
            return this;
        }
        public Builder propertyValue(double val){
            propertyValue = val;
            return this;
        }
        public Builder propertyMonthlyRent(double val){
            propertyMonthlyRent = val;
            return this;
        }
        public Builder propertyCloseByCommercial(boolean val){
            propertyCloseByCommercial = val;
            return this;
        }

        public LoanRequest build(){
            return new LoanRequest(this);
        }
    }


}
