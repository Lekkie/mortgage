package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_product_types")
@Cacheable(true)
@SuppressWarnings("serial")
public class ProductType extends BaseEntity  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productTypeId;
    @Column(name = "product_type_code", nullable = false, unique = false)
    private String productTypeCode;
    @Column(name = "product_type_name", nullable = false, unique = false)
    private String productTypeName;
    @Column(name = "description", nullable = false, unique = false)
    private String description;

    public ProductType(){

    }

    public ProductType(Builder builder){
        this.productTypeId = builder.productTypeId;
        this.productTypeCode = builder.productTypeCode;
        this.productTypeName = builder.productTypeName;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }



    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder{

        private Long productTypeId;
        private String productTypeCode;
        private String productTypeName;
        private String description;
        private String createdBy;
        private Date createdOn;


        public Builder productTypeId(Long val){
            productTypeId = val;
            return this;
        }
        public Builder productTypeCode(String val){
            productTypeCode = val;
            return this;
        }
        public Builder productTypeName(String val){
            productTypeName = val;
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

        public ProductType build(){
            return new ProductType(this);
        }
    }


}
