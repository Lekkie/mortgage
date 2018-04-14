package com.landbay.mortgage.service;

import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by lekanomotayo on 11/04/2018.
 */


public class InterestServiceTest {

    InterestService interestService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() throws Exception{
        interestService = new InterestService();
    }

    @Test
    public void shouldReturnRightValueForSimpleMonthlyInterestValidArguments(){

        BigDecimal amount = new BigDecimal(200000.00);
        BigDecimal rateInPercent = new BigDecimal(4.49);
        int termsInMonths = 1;

        BigDecimal actual = interestService.getSimpleMonthlyInterest(amount, rateInPercent, termsInMonths);

        BigDecimal expected = new BigDecimal(748.33).setScale(2, RoundingMode.HALF_EVEN);
        assertThat(actual, is(expected));
    }


    @Test
    public void shouldThrowExceptionForAnySimpleMonthlyInterestWrongArgument(){
        BigDecimal amount = new BigDecimal(200000.00);
        BigDecimal rateInPercent = new BigDecimal(0);
        int termsInMonths = 1;

        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Invalid arguments passed");
        BigDecimal actual = interestService.getSimpleMonthlyInterest(amount, rateInPercent, termsInMonths);
    }


    @Test
    public void shouldReturnRightValueForSimpleDailyInterestValidArguments(){
        BigDecimal amount = new BigDecimal(200000.00);
        BigDecimal rateInPercent = new BigDecimal(4.49);
        int termsInDays = 30;

        BigDecimal actual = interestService.getSimpleDailyInterest(amount, rateInPercent, termsInDays);

        BigDecimal expected = new BigDecimal(738.08).setScale(2, RoundingMode.HALF_EVEN);
        assertThat(actual, is(expected));
    }



    @Test
    public void shouldThrowExceptionForAnySimpleDailyInterestWrongArgument(){
        BigDecimal amount = new BigDecimal(0);
        BigDecimal rateInPercent = new BigDecimal(4.49);
        int termsInDays = 30;

        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Invalid arguments passed");
        BigDecimal actual = interestService.getSimpleDailyInterest(amount, rateInPercent, termsInDays);
    }



    @Test
    public void shouldReturnRightValueForMonthlyPaymentAmountValidArguments(){
        BigDecimal amount = new BigDecimal(200000.00);
        BigDecimal rateInPercent = new BigDecimal(4.49);
        int termsInMonths = 300;

        BigDecimal actual = interestService.getMonthlyPaymentAmount(amount, rateInPercent, termsInMonths);

        BigDecimal expected = new BigDecimal(1110.45).setScale(2, RoundingMode.HALF_EVEN);
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldThrowExceptionForAnyMonthlyPaymentAmountWrongArgument(){
        BigDecimal amount = new BigDecimal(200000.00);
        BigDecimal rateInPercent = new BigDecimal(4.49);
        int termsInMonths = 0;

        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Invalid arguments passed");
        BigDecimal actual = interestService.getMonthlyPaymentAmount(amount, rateInPercent, termsInMonths);
    }

}
