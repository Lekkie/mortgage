package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_property_types")
@Cacheable(true)
@SuppressWarnings("serial")
public class PropertyType extends BaseEntity {

    @Id
    @Column(name = "property_type_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long propertyTypeId;
    @Column(name = "property_type_code", nullable = false, unique = false)
    private String propertyTypeCode;
    @Column(name = "property_type_name", nullable = false, unique = false)
    private String propertyTypeName;
    @Column(name = "description", nullable = false, unique = false)
    private String description;


    public PropertyType(){

    }

    public PropertyType(Builder builder){
        this.propertyTypeId = builder.propertyTypeId;
        this.propertyTypeCode = builder.propertyTypeCode;
        this.propertyTypeName = builder.propertyTypeName;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder{

        private Long propertyTypeId;
        private String propertyTypeCode;
        private String propertyTypeName;
        private String description;
        private String createdBy;
        private Date createdOn;


        public Builder propertyTypeId(Long val){
            propertyTypeId = val;
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

        public PropertyType build(){
            return new PropertyType(this);
        }
    }

}
