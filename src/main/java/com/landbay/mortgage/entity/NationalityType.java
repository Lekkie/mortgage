package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_nationality_types")
@Cacheable(true)
@SuppressWarnings("serial")
public class NationalityType extends BaseEntity  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long nationalityTypeId;
    @Column(name = "nationality_type_code", nullable = false, unique = false)
    private String nationalityTypeCode;
    @Column(name = "nationality_type_name", nullable = false, unique = false)
    private String nationalityTypeName;
    @Column(name = "description", nullable = false, unique = false)
    private String description;

    public Long getNationalityTypeId() {
        return nationalityTypeId;
    }

    public void setNationalityTypeId(Long nationalityTypeId) {
        this.nationalityTypeId = nationalityTypeId;
    }

    public String getNationalityTypeCode() {
        return nationalityTypeCode;
    }

    public void setNationalityTypeCode(String nationalityTypeCode) {
        this.nationalityTypeCode = nationalityTypeCode;
    }

    public String getNationalityTypeName() {
        return nationalityTypeName;
    }

    public void setNationalityTypeName(String nationalityTypeName) {
        this.nationalityTypeName = nationalityTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
