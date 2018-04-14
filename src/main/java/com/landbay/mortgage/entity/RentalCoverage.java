package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_rental_coverages")
@Cacheable(true)
@SuppressWarnings("serial")
public class RentalCoverage extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rentalCoverageId;
    @Column(name = "product_type_id", nullable = false, unique = false)
    private Long productTypeId;
    @Column(name = "borrower_type_id", nullable = false, unique = false)
    private Long borrowerTypeId;
    @Column(name = "property_type_id", nullable = false, unique = false)
    private Long propertyTypeId;
    @Column(name = "rental_coverage", nullable = false, unique = false)
    private double rentalCoverage;

    public RentalCoverage(){

    }

    public RentalCoverage(Builder builder){
        this.rentalCoverageId = builder.rentalCoverageId;
        this.productTypeId = builder.productTypeId;
        this.borrowerTypeId = builder.borrowerTypeId;
        this.propertyTypeId = builder.propertyTypeId;
        this.rentalCoverage = builder.rentalCoverage;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }


    public Long getRentalCoverageId() {
        return rentalCoverageId;
    }

    public void setRentalCoverageId(Long rentalCoverageId) {
        this.rentalCoverageId = rentalCoverageId;
    }

    public Long getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(Long borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public double getRentalCoverage() {
        return rentalCoverage;
    }

    public void setRentalCoverage(double rentalCoverage) {
        this.rentalCoverage = rentalCoverage;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }


    public static class Builder{

        private Long rentalCoverageId;
        private Long productTypeId;
        private Long borrowerTypeId;
        private Long propertyTypeId;
        private double rentalCoverage;
        private String createdBy;
        private Date createdOn;


        public Builder rentalCoverageId(Long val){
            rentalCoverageId = val;
            return this;
        }
        public Builder productTypeId(Long val){
            productTypeId = val;
            return this;
        }
        public Builder borrowerTypeId(Long val){
            borrowerTypeId = val;
            return this;
        }
        public Builder propertyTypeId(Long val){
            propertyTypeId = val;
            return this;
        }
        public Builder rentalCoverage(double val){
            rentalCoverage = val;
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

        public RentalCoverage build(){
            return new RentalCoverage(this);
        }
    }


}
