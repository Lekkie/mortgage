package com.landbay.mortgage.dto;

/**
 * Created by lekanomotayo on 11/04/2018.
 */
public final class InterestInfo{

    private final double amount;

    public InterestInfo(double amount){
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestInfo interestInfo = (InterestInfo) o;

        return amount == interestInfo.amount;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ((int) ((Double.doubleToLongBits(amount)) ^ (Double.doubleToLongBits(amount)) >>> 32));

        return result;
    }


    @Override
    public String toString() {
        return "InterestInfo{" +
                "amount='" + amount + '\'' +
                '}';
    }



}
