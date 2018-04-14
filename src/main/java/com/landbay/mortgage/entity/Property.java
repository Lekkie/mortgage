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
@Table(name = "tbl_propertys")
@Cacheable(true)
@SuppressWarnings("serial")
public class Property extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long propertyId;
    @Column(name = "property_type_id", nullable = false, unique = false)
    private Long propertyTypeId;
    @Column(name = "post_code", nullable = false, unique = false)
    private String postcode;
    @Column(name = "county", nullable = false, unique = false)
    private String county;
    @Column(name = "beds", nullable = false, unique = false)
    private int beds;
    @Column(name = "property_value", nullable = false, unique = false)
    private double propertyValue;
    @Column(name = "monthly_rent", nullable = false, unique = false)
    private double monthlyRent;
    @Column(name = "rental_coverage", nullable = false)
    private double rentalCoverage;


    public Property(){

    }

    public Property(Builder builder){
        this.propertyId = builder.propertyId;
        this.propertyTypeId = builder.propertyTypeId;
        this.postcode = builder.postcode;
        this.county = builder.county;
        this.beds = builder.beds;
        this.propertyValue = builder.propertyValue;
        this.monthlyRent = builder.monthlyRent;
        this.rentalCoverage = builder.rentalCoverage;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }


    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public double getRentalCoverage() {
        return rentalCoverage;
    }

    public void setRentalCoverage(double rentalCoverage) {
        this.rentalCoverage = rentalCoverage;
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }


    public static class Builder{

        private Long propertyId;
        private Long propertyTypeId;
        private String postcode;
        private String county;
        private int beds;
        private double propertyValue;
        private double monthlyRent;
        private double rentalCoverage;
        private String createdBy;
        private Date createdOn;


        public Builder propertyId(Long val){
            propertyId = val;
            return this;
        }
        public Builder propertyTypeId(Long val){
            propertyTypeId = val;
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
        public Builder beds(int val){
            beds = val;
            return this;
        }
        public Builder propertyValue(double val){
            propertyValue = val;
            return this;
        }
        public Builder monthlyRent(double val){
            monthlyRent = val;
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

        public Property build(){
            return new Property(this);
        }
    }

}
