package com.landbay.mortgage.controller;

import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.InvestmentRequest;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import com.landbay.mortgage.exceptions.EntityNotFoundException;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.InvestmentApplicationFailureException;
import com.landbay.mortgage.exceptions.LoanApplicationFailureException;
import com.landbay.mortgage.service.InterestService;
import com.landbay.mortgage.service.LoanInvestmentService;
import com.landbay.mortgage.service.LoanService;
import com.landbay.mortgage.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

/**
 * Created by lekanomotayo on 13/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class InvestmentControllerTest {

    private InvestmentController investmentController;

    @Mock
    private LoanInvestmentService loanInvestmentService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void setup() throws Exception {
        investmentController = new InvestmentController();
        TestUtils.setField(investmentController, "loanInvestmentService", loanInvestmentService);
    }



    @Test
    public void shouldReturnLoanInvestmentResponseEntityForCreatedInvestment() {

        Date createdOn = new Date();
        long lenderId = 1;
        long loanId = 1;
        BigDecimal amt = new BigDecimal(10000);
        int termsInMonth = 24;

        InvestmentRequest investmentRequest = new InvestmentRequest.Builder()
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termInMonths(termsInMonth)
                .build();

        LoanInvestment loanInvestment = new LoanInvestment.Builder()
                //.loanInvestmentId(1L)
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termsInMonths(termsInMonth)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.create(Matchers.<LoanInvestment>any()))
                .willReturn(Optional.ofNullable(loanInvestment));

        ResponseEntity responseEntity =  investmentController.create(investmentRequest);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(LoanInvestment.class));
        LoanInvestment actual = (LoanInvestment) body;

        assertThat(actual, is(loanInvestment));
    }




    @Test
    public void shouldReturnReturnIllegalMortgageArgumentExceptionForWrongAmount() {

        Date createdOn = new Date();
        long lenderId = 1;
        long loanId = 1;
        BigDecimal amt = new BigDecimal(0);
        int termsInMonth = 24;

        InvestmentRequest investmentRequest = new InvestmentRequest.Builder()
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termInMonths(termsInMonth)
                .build();

        LoanInvestment loanInvestment = new LoanInvestment.Builder()
                .loanInvestmentId(1L)
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termsInMonths(termsInMonth)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.create(Matchers.<LoanInvestment>any()))
                .willReturn(Optional.ofNullable(loanInvestment));


        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Missing investment amount");
        ResponseEntity responseEntity =  investmentController.create(investmentRequest);
    }


    @Test
    public void shouldReturnReturnInvestmentApplicationFailureExceptionWhenUnableToCreateNewInvestment() {

        Date createdOn = new Date();
        long lenderId = 1;
        long loanId = 1;
        BigDecimal amt = new BigDecimal(10000);
        int termsInMonth = 24;

        InvestmentRequest investmentRequest = new InvestmentRequest.Builder()
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termInMonths(termsInMonth)
                .build();

        LoanInvestment loanInvestment = new LoanInvestment.Builder()
                .loanInvestmentId(1L)
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termsInMonths(termsInMonth)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.create(Matchers.<LoanInvestment>any()))
                .willReturn(Optional.empty());


        exception.expect(InvestmentApplicationFailureException.class);
        exception.expectMessage("Unable to create investment application, please contact support.");
        ResponseEntity responseEntity =  investmentController.create(investmentRequest);
    }




}