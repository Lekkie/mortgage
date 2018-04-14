package com.landbay.mortgage.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landbay.mortgage.config.EnvironmentDataConfig;
import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.LoanInfo;
import com.landbay.mortgage.dto.LoanRequest;
import com.landbay.mortgage.entity.*;
import com.landbay.mortgage.service.*;
import com.landbay.mortgage.type.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lekanomotayo on 13/04/2018.
 */


@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LenderController.class)
public class LenderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;
    @MockBean
    private LoanInvestmentService loanInvestmentService;
    @MockBean
    private InterestService interestService;


    @Before
    public void setup() throws Exception {
    }

    @Test
    public void shouldReturnMonthlyInterestInfoResponseEntityForInterestByLenderId() throws Exception{

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
        given(interestService.getSimpleMonthlyInterest(new BigDecimal(10000), lenderFixedRate, 1))
                .willReturn(new BigDecimal(29.08));


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        InterestInfo expected = new InterestInfo(29.08);

        mockMvc.perform(get("/api/v1/lenders/1/interests"))
                .andExpect(content().string(mapper.writeValueAsString(expected)))
                .andExpect(status().isOk());

    }


    @Test
    public void shouldReturnDailyInterestInfoResponseEntityForInterestByLenderId() throws Exception{

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
        given(interestService.getSimpleDailyInterest(new BigDecimal(10000), lenderFixedRate, 30))
                .willReturn(new BigDecimal(28.68));


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        InterestInfo expected = new InterestInfo(28.68);

        mockMvc.perform(get("/api/v1/lenders/1/interests?startDate=2018-03-12&endDate=2018-04-11"))
                .andExpect(content().string(mapper.writeValueAsString(expected)))
                .andExpect(status().isOk());

    }


    @Test
    public void shouldReturnReturnErrorObjectForWrongLenderId() throws Exception{

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
        given(interestService.getSimpleDailyInterest(new BigDecimal(10000), lenderFixedRate, 30))
                .willReturn(new BigDecimal(28.68));
        given(interestService.getSimpleMonthlyInterest(new BigDecimal(10000), lenderFixedRate, 1))
                .willReturn(new BigDecimal(29.08));


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Error expected = new Error(1002, "Loan Investment for Lender 1 not found");

        mockMvc.perform(get("/api/v1/lenders/1/interests?startDate=2018-03-12&endDate=2018-04-11"))
                .andExpect(content().string(mapper.writeValueAsString(expected)))
                .andExpect(status().isBadRequest());

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