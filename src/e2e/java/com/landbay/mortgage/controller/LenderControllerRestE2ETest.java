package com.landbay.mortgage.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landbay.mortgage.config.EnvironmentDataConfig;
import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.dto.LoanInfo;
import com.landbay.mortgage.dto.LoanRequest;
import com.landbay.mortgage.entity.*;
import com.landbay.mortgage.service.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class LenderControllerRestE2ETest {


    @Autowired
    private LoanService loanService;
    @Autowired
    private InterestService interestService;
    @Autowired
    private LoanInvestmentService loanInvestmentService;

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws Exception {
    }


    @After
    public void tearDown() throws Exception {
        loanService.deleteAll();
        loanInvestmentService.deleteAll();
    }


    @Test
    public void shouldReturnMonthlyInterestInfoResponseEntityForInterestByLenderId() throws Exception{

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal borrowerRate = new BigDecimal(3.09).add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(300);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        Loan loan = new Loan.Builder()
                //.loanId(1L)
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
        loan = loanService.create(loan).orElse(null);

        List<LoanInvestment> loanInvestmentList = getLoanInvestments(loan.getLoanId(), createdOn);
        List<LoanInvestment> savedLoanInvestmentList = new ArrayList<>();
        for(LoanInvestment loanInvestment: loanInvestmentList)
            loanInvestmentService.create(loanInvestment);


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        InterestInfo expected = new InterestInfo(29.08);

        Response response = when()
                .get(String.format("http://localhost:%s/api/v1/lenders/1/interests", port))
                .then()
                .contentType(containsString("application/json"))
                //.statusCode(is(200))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));

    }


    @Test
    public void shouldReturnDailyInterestInfoResponseEntityForInterestByLenderId() throws Exception{

        Date createdOn = new Date();
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
        loan = loanService.create(loan).orElse(null);

        List<LoanInvestment> loanInvestmentList = getLoanInvestments(loan.getLoanId(), createdOn);
        for(LoanInvestment loanInvestment: loanInvestmentList)
            loanInvestmentService.create(loanInvestment);



        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        InterestInfo expected = new InterestInfo(28.68);

        Response response = when()
                .get(String.format("http://localhost:%s/api/v1/lenders/1/interests?startDate=2018-03-12&endDate=2018-04-11", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(200))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }


    @Test
    public void shouldReturnReturnErrorObjectForWrongLenderId() throws Exception{

        Date createdOn = new Date();
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

        loanService.create(loan);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Error expected = new Error(1002, "Loan Investment for Lender 1 not found");

        Response response = when()
                .get(String.format("http://localhost:%s/api/v1/lenders/1/interests?startDate=2018-03-12&endDate=2018-04-11", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(400))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }




    private List<LoanInvestment> getLoanInvestments(Long loanId, Date createdOn){

        List<LoanInvestment> loanInvestmentList = new ArrayList<>();
        LoanInvestment loanInvestment1 = new LoanInvestment.Builder()
                .loanInvestmentId(1L)
                .loanId(loanId)
                .lenderId(1L)
                .amount(new BigDecimal(10000))
                .termsInMonths(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
        loanInvestmentList.add(loanInvestment1);

        return loanInvestmentList;
    }

}