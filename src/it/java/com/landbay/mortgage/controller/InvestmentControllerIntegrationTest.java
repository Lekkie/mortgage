package com.landbay.mortgage.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.InvestmentRequest;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import com.landbay.mortgage.exceptions.InvestmentApplicationFailureException;
import com.landbay.mortgage.service.InterestService;
import com.landbay.mortgage.service.LoanInvestmentService;
import com.landbay.mortgage.service.LoanService;
import com.landbay.mortgage.type.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
@WebMvcTest(controllers = InvestmentController.class)
public class InvestmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanInvestmentService loanInvestmentService;


    @Before
    public void setup() throws Exception {
    }

    @Test
    public void shouldReturnLoanInvestmentResponseEntityForCreatedInvestment() throws Exception{

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


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mockMvc.perform(post("/api/v1/investments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(investmentRequest)))
                .andExpect(content().string(mapper.writeValueAsString(loanInvestment)))
                .andExpect(status().isCreated());

    }


    @Test
    public void shouldReturnInvestmentApplicationFailureExceptionWhenUnableToCreateNewInvestment() throws Exception{

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Error expected = new Error(1005, "Loan Application failure: Unable to create investment application, please contact support.");

        mockMvc.perform(post("/api/v1/investments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(investmentRequest)))
                .andExpect(content().string(mapper.writeValueAsString(expected)))
                .andExpect(status().isBadRequest());
    }


}