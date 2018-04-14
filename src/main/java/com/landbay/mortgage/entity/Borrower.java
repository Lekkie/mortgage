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
@Table(name = "tbl_borrowers")
@Cacheable(true)
@SuppressWarnings("serial")
public class Borrower  extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "borrower_id", nullable = false, unique = true)
    private Long borrowerId;
    @Column(name = "borrower_type_id", nullable = false)
    private Long borrowerTypeId;
    @Column(name = "firstname", nullable = false, unique = false)
    private String firstname;
    @Column(name = "lastname", nullable = false, unique = false)
    private String lastname;
    @Column(name = "salary", nullable = false, unique = false)
    private double salary;
    @Column(name = "nationality", nullable = false, unique = false)
    private String nationality;
    @Column(name = "age", nullable = false, unique = false)
    private int age;
    @Column(name = "first_time_landlord", nullable = false, unique = false)
    private boolean firstTimeLandlord;
    @Column(name = "first_time_buyer", nullable = false, unique = false)
    private boolean firstTimeBuyer;
    @Column(name = "active_bank_ccount", nullable = false, unique = false)
    private boolean activeBankAccount;
    @Column(name = "uk_credit_history", nullable = false, unique = false)
    private boolean ukCreditHistory;


    public Borrower(){

    }

    public Borrower(Builder builder){
        this.borrowerId = builder.borrowerId;
        this.borrowerTypeId = builder.borrowerTypeId;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.salary = builder.salary;
        this.nationality = builder.nationality;
        this.age = builder.age;
        this.firstTimeLandlord = builder.firstTimeLandlord;
        this.firstTimeBuyer = builder.firstTimeBuyer;
        this.activeBankAccount = builder.activeBankAccount;
        this.ukCreditHistory = builder.ukCreditHistory;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }



    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFirstTimeLandlord() {
        return firstTimeLandlord;
    }

    public void setFirstTimeLandlord(boolean firstTimeLandlord) {
        this.firstTimeLandlord = firstTimeLandlord;
    }

    public boolean isFirstTimeBuyer() {
        return firstTimeBuyer;
    }

    public void setFirstTimeBuyer(boolean firstTimeBuyer) {
        this.firstTimeBuyer = firstTimeBuyer;
    }

    public boolean isActiveBankAccount() {
        return activeBankAccount;
    }

    public void setActiveBankAccount(boolean activeBankAccount) {
        this.activeBankAccount = activeBankAccount;
    }

    public boolean isUkCreditHistory() {
        return ukCreditHistory;
    }

    public void setUkCreditHistory(boolean ukCreditHistory) {
        this.ukCreditHistory = ukCreditHistory;
    }

    public Long getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(Long borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public static class Builder{

        private Long borrowerId;
        private Long borrowerTypeId;
        private String firstname;
        private String lastname;
        private double salary;
        private String nationality;
        private int age;
        private boolean firstTimeLandlord;
        private boolean firstTimeBuyer;
        private boolean activeBankAccount;
        private boolean ukCreditHistory;
        private String createdBy;
        private Date createdOn;

        public Builder borrowerId(Long val){
            borrowerId = val;
            return this;
        }
        public Builder borrowerTypeId(Long val){
            borrowerTypeId = val;
            return this;
        }
        public Builder firstname(String val){
            firstname = val;
            return this;
        }
        public Builder lastname(String val){
            lastname = val;
            return this;
        }
        public Builder salary(double val){
            salary = val;
            return this;
        }
        public Builder nationality(String val){
            nationality = val;
            return this;
        }
        public Builder age(int val){
            age = val;
            return this;
        }
        public Builder firstTimeLandlord(boolean val){
            firstTimeLandlord = val;
            return this;
        }
        public Builder firstTimeBuyer(boolean val){
            firstTimeBuyer = val;
            return this;
        }
        public Builder activeBankAccount(boolean val){
            activeBankAccount = val;
            return this;
        }
        public Builder ukCreditHistory(boolean val){
            ukCreditHistory = val;
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

        public Borrower build(){
            return new Borrower(this);
        }
    }

}
