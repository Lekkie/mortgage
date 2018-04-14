package com.landbay.mortgage.controller;

import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.LoanInfo;
import com.landbay.mortgage.dto.LoanRequest;
import com.landbay.mortgage.entity.*;
import com.landbay.mortgage.exceptions.EntityNotFoundException;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.LoanApplicationFailureException;
import com.landbay.mortgage.service.*;
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
import java.math.RoundingMode;
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
public class LenderControllerTest {

    private LenderController lenderController;

    @Mock
    private LoanService loanService;
    @Mock
    private LoanInvestmentService loanInvestmentService;

    InterestService interestService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void setup() throws Exception {
        lenderController = new LenderController();
        interestService = new InterestService();
        TestUtils.setField(lenderController, "interestService", interestService);
        TestUtils.setField(lenderController, "loanInvestmentService", loanInvestmentService);
        TestUtils.setField(lenderController, "loanService", loanService);
    }





    @Test
    public void shouldReturnMonthlyInterestInfoResponseEntityForInterestByLenderId() {

        Date createdOn = new Date();
        long lenderId = 1;
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(1L, lenderId, createdOn);
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal borrowerRate = new BigDecimal(3.09).add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(300);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(1L)
                .propertyId(1L)
                .productId(1L)
                .purpose("")
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(250000).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(75)
                .tenureInMonths(300)
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.findByLenderId(lenderId))
                .willReturn(Optional.ofNullable(loanInvestmentList));
        given(loanService.findByLoanId(Matchers.<Long>any()))
                .willReturn(Optional.ofNullable(loan));

        ResponseEntity responseEntity =  lenderController.getInterestByLenderId(lenderId, null, null);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(InterestInfo.class));
        InterestInfo actual = (InterestInfo) body;

        InterestInfo expected = new InterestInfo(29.08);

        assertThat(actual, is(expected));
    }


    @Test
    public void shouldReturnDailyInterestInfoResponseEntityForInterestByLenderId() {

        Date createdOn = new Date();
        long lenderId = 1;
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(1L, lenderId, createdOn);
        BigDecimal loanAmt = new BigDecimal(187500.0);
        BigDecimal borrowerRate = new BigDecimal(3.09).add(new BigDecimal(1.0));
        BigDecimal lenderFixedRate = new BigDecimal(3.49);
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(300);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(1L)
                .propertyId(1L)
                .productId(1L)
                .purpose("")
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(250000).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(75)
                .tenureInMonths(300)
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.findByLenderId(lenderId))
                .willReturn(Optional.ofNullable(loanInvestmentList));
        given(loanService.findByLoanId(Matchers.<Long>any()))
                .willReturn(Optional.ofNullable(loan));

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date startDate = new Date(System.currentTimeMillis() - (30 * DAY_IN_MS));
        Date endDate = new Date();
        ResponseEntity responseEntity =  lenderController.getInterestByLenderId(lenderId, startDate, endDate);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(InterestInfo.class));
        InterestInfo actual = (InterestInfo) body;

        InterestInfo expected = new InterestInfo(28.68);
        assertThat(actual, is(expected));
    }





    @Test
    public void shouldReturnReturnEntityNotFoundExceptionForWrongLenderId() {

        Date createdOn = new Date();
        long lenderId = 1;
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(1L, lenderId, createdOn);
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal borrowerRate = new BigDecimal(3.09).add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(300);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(1L)
                .propertyId(1L)
                .productId(1L)
                .purpose("")
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(250000).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(75)
                .tenureInMonths(300)
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(loanInvestmentService.findByLenderId(lenderId))
                .willReturn(Optional.empty());
        given(loanService.findByLoanId(Matchers.<Long>any()))
                .willReturn(Optional.ofNullable(loan));

        exception.expect(EntityNotFoundException.class);
        exception.expectMessage("Loan Investment for Lender 1 not found");
        ResponseEntity responseEntity =  lenderController.getInterestByLenderId(lenderId, null, null);
    }





    private List<LoanInvestment> getLoanInvestments(Long loanId, Long lenderId, Date createdOn){

        List<LoanInvestment> loanInvestmentList = new ArrayList<>();
        LoanInvestment loanInvestment1 = new LoanInvestment.Builder()
                .loanInvestmentId(1L)
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(new BigDecimal(10000))
                .termsInMonths(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
        loanInvestmentList.add(loanInvestment1);

        return loanInvestmentList;
    }

}