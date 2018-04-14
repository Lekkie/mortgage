package com.landbay.mortgage.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_lenders")
@Cacheable(true)
@SuppressWarnings("serial")
public class Lender extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long lenderId;
    @Column(name = "amount", nullable = false)
    private Long amount;


    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
