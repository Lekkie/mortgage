package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_borrower_types")
@Cacheable(true)
@SuppressWarnings("serial")
public class BorrowerType extends BaseEntity  {

    @Id
    @Column(name = "borrower_type_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long borrowerTypeId;
    @Column(name = "borrower_type_code", nullable = false, unique = false)
    private String borrowerTypeCode;
    @Column(name = "borrower_type_name", nullable = false, unique = false)
    private String borrowerTypeName;
    @Column(name = "description", nullable = false, unique = false)
    private String description;


    public BorrowerType(){

    }

    public BorrowerType(Builder builder){
        this.borrowerTypeId = builder.borrowerTypeId;
        this.borrowerTypeCode = builder.borrowerTypeCode;
        this.borrowerTypeName = builder.borrowerTypeName;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }


    public Long getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(Long borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public String getBorrowerTypeCode() {
        return borrowerTypeCode;
    }

    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    public String getBorrowerTypeName() {
        return borrowerTypeName;
    }

    public void setBorrowerTypeName(String borrowerTypeName) {
        this.borrowerTypeName = borrowerTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder{

        private Long borrowerTypeId;
        private String borrowerTypeCode;
        private String borrowerTypeName;
        private String description;
        private String createdBy;
        private Date createdOn;


        public Builder borrowerTypeId(Long val){
            borrowerTypeId = val;
            return this;
        }
        public Builder borrowerTypeCode(String val){
            borrowerTypeCode = val;
            return this;
        }
        public Builder borrowerTypeName(String val){
            borrowerTypeName = val;
            return this;
        }
        public Builder description(String val){
            description = val;
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

        public BorrowerType build(){
            return new BorrowerType(this);
        }
    }



}
