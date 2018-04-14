package com.landbay.mortgage.service;

import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.IllegalMortgageStateException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@Service
public class InterestService {


    public BigDecimal getMonthlyPaymentAmount(BigDecimal amt, BigDecimal rateInPercent, int termInMonths){

        if(amt == null || amt.doubleValue() == 0
                || rateInPercent == null || rateInPercent.doubleValue() == 0
                || termInMonths == 0)
            throw new IllegalMortgageArgumentException("Invalid arguments passed");

        BigDecimal rate = rateInPercent.divide(new BigDecimal(100));
        BigDecimal monthsBD = new BigDecimal(12);
        BigDecimal monthlyRateBD = rate.divide(monthsBD, RoundingMode.HALF_EVEN);

        BigDecimal oneBD = new BigDecimal(1);
        BigDecimal monthlyRateAmtBD = amt.multiply(monthlyRateBD);
        BigDecimal onePlusMonthRateBD = monthlyRateBD.add(oneBD);
        MathContext mc = new MathContext(4);
        BigDecimal powBD = onePlusMonthRateBD.pow(-termInMonths, mc);
        BigDecimal oneMinusPowBD = oneBD.subtract(powBD);
        BigDecimal monthlyPaymentBD = monthlyRateAmtBD.divide(oneMinusPowBD, RoundingMode.HALF_EVEN);
        monthlyPaymentBD = monthlyPaymentBD.setScale(2, RoundingMode.HALF_EVEN);

        return monthlyPaymentBD;
    }


    // Monthly Simple interest
    public BigDecimal getSimpleMonthlyInterest(BigDecimal amt, BigDecimal rateInPercent, int termInMonths){

        if(amt == null || amt.doubleValue() == 0
                || rateInPercent == null || rateInPercent.doubleValue() == 0
                || termInMonths == 0)
            throw new IllegalMortgageArgumentException("Invalid arguments passed");

        int workingDecimalPlaces = 12;
        BigDecimal rate = rateInPercent.divide(new BigDecimal(100)).setScale(workingDecimalPlaces, RoundingMode.HALF_EVEN);
        BigDecimal monthsInYear = new BigDecimal(12).setScale(workingDecimalPlaces);
        BigDecimal monthlyInterestRate = rate.divide(monthsInYear, RoundingMode.HALF_EVEN);

        BigDecimal termInMonthsBigDecimal = new BigDecimal(termInMonths);
        BigDecimal monthlyInterestRateTerm = monthlyInterestRate.multiply(termInMonthsBigDecimal);
        BigDecimal one = new BigDecimal(1);
        BigDecimal onePlusMonthlyInterestRate = monthlyInterestRateTerm.add(one);
        BigDecimal amtTmp = amt.setScale(workingDecimalPlaces, RoundingMode.HALF_EVEN);
        BigDecimal totalAmount = amtTmp.multiply(onePlusMonthlyInterestRate);
        BigDecimal monthlyInterest = totalAmount.subtract(amtTmp).setScale(2, RoundingMode.HALF_EVEN);

        return monthlyInterest;
    }

    // Daily Simple interest
    public BigDecimal getSimpleDailyInterest(BigDecimal amt, BigDecimal rateInPercent, int termInDays){

        if(amt == null || amt.doubleValue() == 0
                || rateInPercent == null || rateInPercent.doubleValue() == 0
                || termInDays == 0)
            throw new IllegalMortgageArgumentException("Invalid arguments passed");

        int workingDecimalPlaces = 12;
        BigDecimal rate = rateInPercent.divide(new BigDecimal(100)).setScale(workingDecimalPlaces, RoundingMode.HALF_EVEN);
        BigDecimal daysInYear = new BigDecimal(365).setScale(workingDecimalPlaces);
        BigDecimal dailyInterestRate = rate.divide(daysInYear, RoundingMode.HALF_EVEN);

        BigDecimal termInDaysBigDecimal = new BigDecimal(termInDays);
        BigDecimal dailyInterestRateTerm = dailyInterestRate.multiply(termInDaysBigDecimal);
        BigDecimal one = new BigDecimal(1);
        BigDecimal onePlusDailyInterestRate = dailyInterestRateTerm.add(one);
        BigDecimal amtTmp = amt.setScale(workingDecimalPlaces, RoundingMode.HALF_EVEN);
        BigDecimal totalAmount = amtTmp.multiply(onePlusDailyInterestRate);
        BigDecimal dailyInterest = totalAmount.subtract(amtTmp).setScale(2, RoundingMode.HALF_EVEN);

        return dailyInterest;
    }


}
