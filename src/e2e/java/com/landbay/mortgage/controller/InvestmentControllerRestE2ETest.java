package com.landbay.mortgage.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.InvestmentRequest;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import com.landbay.mortgage.service.InterestService;
import com.landbay.mortgage.service.LoanInvestmentService;
import com.landbay.mortgage.service.LoanService;
import com.landbay.mortgage.type.End2EndTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

/**
 * Created by lekanomotayo on 13/04/2018.
 */


@Category(End2EndTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvestmentControllerRestE2ETest {


    @Autowired
    private LoanInvestmentService loanInvestmentService;

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        loanInvestmentService.deleteAll();
    }


    @Test
    public void shouldReturnIllegalMortgageArgumentExceptionWhenUnableToCreateNewInvestment() throws Exception{

        Date createdOn = new Date();
        long lenderId = 1;
        long loanId = 1;
        BigDecimal amt = new BigDecimal(10000);
        int termsInMonth = 24;

        InvestmentRequest investmentRequest = new InvestmentRequest.Builder().build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Error expected = new Error(1001, "Missing lender");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(investmentRequest)
                .when()
                .post(String.format("http://localhost:%s/api/v1/investments", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(400))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }


    @Test
    public void shouldReturnDailyInterestInfoResponseEntityForInterestByLenderId() throws Exception{

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LoanInvestment expected = new LoanInvestment.Builder()
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(amt)
                .termsInMonths(termsInMonth)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(investmentRequest)
                .when()
                .post(String.format("http://localhost:%s/api/v1/investments", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(201))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }


}