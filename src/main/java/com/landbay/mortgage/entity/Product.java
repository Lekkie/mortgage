package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by lekanomotayo on 10/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_products")
@Cacheable(true)
@SuppressWarnings("serial")
public class Product extends BaseEntity {

    public static final transient Comparator<Product> BY_LTV = new ByLtv();

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productId;
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;
    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;
    @Column(name = "tenure", nullable = false, unique = false)
    private int tenure;
    @Column(name = "product_type_id", nullable = false, unique = false)
    private Long productTypeId;
    @Column(name = "property_type_id", nullable = true, unique = false)
    private Long propertyTypeId;
    @Column(name = "nationality_type_id", nullable = false, unique = false)
    private Long nationalityTypeId;
    @Column(name = "fixed_borrowing_rate", nullable = false, unique = false)
    private BigDecimal fixedBorrowingRate;
    @Column(name = "tracker_borrowing_rate", nullable = false, unique = false)
    private BigDecimal trackerBorrowingRate;
    @Column(name = "product_ltv", nullable = false, unique = false)
    private double productLtv;
    @Column(name = "product_fee", nullable = false, unique = false)
    private BigDecimal productFee;
    @Column(name = "description", nullable = false, unique = false)
    private String description;

    public Product(){

    }

    public Product(Builder builder){
        this.productId = builder.productId;
        this.productCode = builder.productCode;
        this.productName = builder.productName;
        this.tenure = builder.tenure;
        this.productTypeId = builder.productTypeId;
        this.propertyTypeId = builder.propertyTypeId;
        this.nationalityTypeId = builder.nationalityTypeId;
        this.fixedBorrowingRate = builder.fixedBorrowingRate;
        this.trackerBorrowingRate = builder.trackerBorrowingRate;
        this.productLtv = builder.productLtv;
        this.productFee = builder.productFee;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdOn = builder.createdOn;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getProductFee() {
        return productFee;
    }

    public void setProductFee(BigDecimal productFee) {
        this.productFee = productFee;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public BigDecimal getFixedBorrowingRate() {
        return fixedBorrowingRate;
    }

    public void setFixedBorrowingRate(BigDecimal fixedBorrowingRate) {
        this.fixedBorrowingRate = fixedBorrowingRate;
    }

    public BigDecimal getTrackerBorrowingRate() {
        return trackerBorrowingRate;
    }

    public void setTrackerBorrowingRate(BigDecimal trackerBorrowingRate) {
        this.trackerBorrowingRate = trackerBorrowingRate;
    }

    public double getProductLtv() {
        return productLtv;
    }

    public void setProductLtv(double productLtv) {
        this.productLtv = productLtv;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public Long getNationalityTypeId() {
        return nationalityTypeId;
    }

    public void setNationalityTypeId(Long nationalityTypeId) {
        this.nationalityTypeId = nationalityTypeId;
    }

    private static class ByLtv implements Comparator<Product> {
        public int compare(Product v, Product w){
            double x = v.productLtv;
            double y = w.productLtv;
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }
    }


    public static class Builder{

        private Long productId;
        private String productCode;
        private String productName;
        private int tenure;
        private Long productTypeId;
        private Long propertyTypeId;
        private Long nationalityTypeId;
        private BigDecimal fixedBorrowingRate;
        private BigDecimal trackerBorrowingRate;
        private double productLtv;
        private BigDecimal productFee;
        private String description;
        private String createdBy;
        private Date createdOn;


        public Builder productId(Long val){
            productId = val;
            return this;
        }
        public Builder productCode(String val){
            productCode = val;
            return this;
        }
        public Builder productName(String val){
            productName = val;
            return this;
        }
        public Builder tenure(int val){
            tenure = val;
            return this;
        }
        public Builder productTypeId(Long val){
            productTypeId = val;
            return this;
        }
        public Builder propertyTypeId(Long val){
            propertyTypeId = val;
            return this;
        }
        public Builder nationalityTypeId(Long val){
            nationalityTypeId = val;
            return this;
        }
        public Builder fixedBorrowingRate(BigDecimal val){
            fixedBorrowingRate = val;
            return this;
        }
        public Builder trackerBorrowingRate(BigDecimal val){
            trackerBorrowingRate = val;
            return this;
        }
        public Builder productLtv(double val){
            productLtv = val;
            return this;
        }
        public Builder productFee(BigDecimal val){
            productFee = val;
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

        public Product build(){
            return new Product(this);
        }
    }
}
