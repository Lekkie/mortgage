package com.landbay.mortgage.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landbay.mortgage.config.EnvironmentDataConfig;
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

import com.landbay.mortgage.dto.Error;

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
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by lekanomotayo on 13/04/2018.
 */


@Category(End2EndTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanControllerRestE2ETest {


    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private RentalCoverageService rentalCoverageService;
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private PropertyTypeService propertyTypeService;
    @Autowired
    private BorrowerTypeService borrowerTypeService;
    @Autowired
    private EnvironmentDataConfig environmentDataConfig;
    @Autowired
    private LoanInvestmentService loanInvestmentService;
    @Autowired
    private InterestService interestService;

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        borrowerService.deleteAll();
        propertyService.deleteAll();
        productService.deleteAll();
        loanService.deleteAll();
        rentalCoverageService.deleteAll();
        productTypeService.deleteAll();
        propertyTypeService.deleteAll();
        borrowerTypeService.deleteAll();
        loanInvestmentService.deleteAll();
    }


    @Test
    public void shouldReturnResponseEntityForCreatedLoan() throws Exception{

        Date createdOn = new Date();
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        ProductType productType = getProductType(createdOn);
        borrowerType = borrowerTypeService.create(borrowerType).orElse(null);
        propertyType = propertyTypeService.create(propertyType).orElse(null);
        productType = productTypeService.create(productType).orElse(null);
        Product product = getProduct(productType.getProductTypeId(), propertyType.getPropertyTypeId(), createdOn);
        productType = productTypeService.create(productType).orElse(null);
        RentalCoverage rentalCoverage = getRentalCoverage(productType.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId(), createdOn);
        rentalCoverage = rentalCoverageService.create(rentalCoverage).orElse(null);
        product = productService.create(product).orElse(null);

        LoanRequest givenLoanRequest = new LoanRequest.Builder()
                .borrowerFirstname("James")
                .borrowerLastname("Watson")
                .borrowerSalary(90000)
                .borrowerNationality("Nigerian")
                .borrowerAge(33)
                .borrowerFirstTimeLandlord(false)
                .borrowerFirstTimeBuyer(false)
                .borrowerActiveBankAccount(true)
                .borrowerUkCreditHistory(true)
                .goodCreditInLastYear(true)
                .productCode("STANDARD_FRY2_75")
                .applicationTypeCode("INDI")
                .loanPurpose("Purchase")
                .loanTenureInMonths(300)
                .propertyPostcode("MK40")
                .propertyCounty("Bedford")
                .propertyTypeCode("STA")
                .propertyTypeName("House - Semi")
                .propertyBeds(4)
                .propertyValue(250000)
                .propertyMonthlyRent(1200)
                .satisfyAssessmentThreshold(true)
                .haveMinimumDeposit(true)
                .borrowerTypeCode("IND")
                .propertyCloseByCommercial(false)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.setDateFormat(df);  // this works for outbounds but has no effect on inbounds
        mapper.getDeserializationConfig().with(df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LoanInfo expected = new LoanInfo.Builder()
                //.loanId(1L)
                .loanAmount(187500.0)
                .borrowerRate(4.09)
                .ltv(75.0)
                .rentalCoverage(140.0)
                .monthlyRepayment(997.96)
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(givenLoanRequest)
                .when()
                .post(String.format("http://localhost:%s/api/v1/loans", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(201))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }


    @Test
    public void shouldReturnLoanInfoResponseEntityForGetLoanId() throws Exception{

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49);

        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        ProductType productType = getProductType(createdOn);
        borrowerType = borrowerTypeService.create(borrowerType).orElse(null);
        propertyType = propertyTypeService.create(propertyType).orElse(null);
        productType = productTypeService.create(productType).orElse(null);
        RentalCoverage rentalCoverage = getRentalCoverage(productType.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId(), createdOn);
        Product product = getProduct(productType.getProductTypeId(), propertyType.getPropertyTypeId(), createdOn);
        product = productService.create(product).orElse(null);
        rentalCoverage = rentalCoverageService.create(rentalCoverage).orElse(null);
        Borrower borrower = getBorrower(borrowerType.getBorrowerTypeId(), createdOn);
        Property property = getProperty(propertyType.getPropertyTypeId(), rentalCoverage.getRentalCoverage(),  createdOn);
        BigDecimal borrowerRate = new BigDecimal(3.09).add(new BigDecimal(1.0));

        borrower = borrowerService.create(borrower).orElse(new Borrower());
        property = propertyService.create(property).orElse(new Property());
        Loan loan = new Loan.Builder()
                //.loanId(100L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose("Purchase")
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
        loan = loanService.create(loan).orElse(new Loan());
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(loan.getLoanId(), createdOn);
        for(LoanInvestment loanInvestment: loanInvestmentList)
            loanInvestmentService.create(loanInvestment);

        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.setDateFormat(df);  // this works for outbounds but has no effect on inbounds
        mapper.getDeserializationConfig().with(df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LoanInfo expected = new LoanInfo.Builder()
                //.loanId(1L)
                .beds(property.getBeds())
                .monthlyRent(property.getMonthlyRent())
                .propertyValue(property.getPropertyValue())
                .county(property.getCounty())
                .postcode(property.getPostcode())
                .rentalCoverage(property.getRentalCoverage())
                .borrowerId(borrower.getBorrowerId())
                .borrowerType("Individual")
                .productType("2 Fixed Rate Years")
                .propertyType("Standard")
                .product("Standard 2 Year Fixed, 75 LTV")
                .borrowerRate(4.09)
                .completionDate(df.format(loan.getCompletionDate()))
                .tenureInMonths(loan.getTenureInMonths())
                .purpose(loan.getPurpose())
                .ltv(loan.getLtv())
                .lenderRate(loan.getLenderRate().doubleValue())
                .loanAmount(loan.getLoanAmount().doubleValue())
                .monthlyRepayment(997.96)
                .investments(loanInvestmentList)
                .build();

        Response response = when()
                .get(String.format("http://localhost:%s/api/v1/loans/" + loan.getLoanId(), port))
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
    public void shouldReturnEntityNotFoundExceptionForWrongProduct() throws Exception{

        LoanRequest givenLoanRequest = new LoanRequest.Builder()
                .borrowerFirstname("James")
                .borrowerLastname("Watson")
                .borrowerSalary(90000)
                .borrowerNationality("Nigerian")
                .borrowerAge(33)
                .borrowerFirstTimeLandlord(false)
                .borrowerFirstTimeBuyer(false)
                .borrowerActiveBankAccount(true)
                .borrowerUkCreditHistory(true)
                .goodCreditInLastYear(true)
                .productCode("STANDARD_FRY2_75_TEST")
                .applicationTypeCode("INDI")
                .loanPurpose("Purchase")
                .loanTenureInMonths(300)
                .propertyPostcode("MK40")
                .propertyCounty("Bedford")
                .propertyTypeCode("STA")
                .propertyTypeName("House - Semi")
                .propertyBeds(4)
                .propertyValue(250000)
                .propertyMonthlyRent(1200)
                .satisfyAssessmentThreshold(true)
                .haveMinimumDeposit(true)
                .borrowerTypeCode("IND")
                .propertyCloseByCommercial(false)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.setDateFormat(df);  // this works for outbounds but has no effect on inbounds
        mapper.getDeserializationConfig().with(df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Error expected = new Error(1002, "Product STANDARD_FRY2_75_TEST not found");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(givenLoanRequest)
                .when()
                .post(String.format("http://localhost:%s/api/v1/loans", port))
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
    public void shouldReturnErrorResponseEntityForUnknownLoanId() throws Exception{

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0);
        int tenureMonths = 300;
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        ProductType productType = getProductType(createdOn);
        borrowerType = borrowerTypeService.create(borrowerType).orElse(null);
        propertyType = propertyTypeService.create(propertyType).orElse(null);
        productType = productTypeService.create(productType).orElse(null);
        RentalCoverage rentalCoverage = getRentalCoverage(productType.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId(), createdOn);
        rentalCoverage = rentalCoverageService.create(rentalCoverage).orElse(null);
        Product product = getProduct(productType.getProductTypeId(), propertyType.getPropertyTypeId(), createdOn);
        Borrower borrower = getBorrower(borrowerType.getBorrowerTypeId(), createdOn);
        Property property = getProperty(propertyType.getPropertyTypeId(), rentalCoverage.getRentalCoverage(), createdOn);

        borrowerService.create(borrower);
        propertyService.create(property);
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(100L, createdOn);
        for(LoanInvestment loanInvestment: loanInvestmentList)
            loanInvestmentService.create(loanInvestment);

        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.setDateFormat(df);  // this works for outbounds but has no effect on inbounds
        mapper.getDeserializationConfig().with(df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Error expected = new Error(1002, "Loan 1 not found");

        Response response = when()
                .get(String.format("http://localhost:%s/api/v1/loans/1", port))
                .then()
                .contentType(containsString("application/json"))
                .statusCode(is(400))
                //.body(is(mapper.writeValueAsString(expected)));
                .extract().response();

        String expectedStr = mapper.writeValueAsString(expected);
        String responseStr = response.asString();
        assertThat(responseStr, Matchers.is(expectedStr));
    }





    private Borrower getBorrower(Long borrowerTypeId, Date createdOn){
        return new Borrower.Builder()
                //.borrowerId(id)
                .borrowerTypeId(borrowerTypeId)
                .firstname("James")
                .lastname("Watson")
                .salary(90000)
                .nationality("Nigerian")
                .age(33)
                .firstTimeLandlord(false)
                .firstTimeBuyer(false)
                .activeBankAccount(true)
                .ukCreditHistory(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();
    }

    private Property getProperty(Long propertyTypeId, double rentalCoverage, Date createdOn){
        return new Property.Builder()
                //.propertyId(id)
                .propertyTypeId(propertyTypeId)
                .postcode("MK40")
                .county("Bedford")
                .beds(4)
                .propertyValue(250000)
                .monthlyRent(1200)
                .rentalCoverage(rentalCoverage)
                .createdBy("User")
                .createdOn(createdOn)
                .build();
    }


    private List<LoanInvestment> getLoanInvestments(Long loanId, Date createdOn){

        List<LoanInvestment> loanInvestmentList = new ArrayList<>();
        LoanInvestment loanInvestment1 = new LoanInvestment.Builder()
                //.loanInvestmentId(id)
                .loanId(loanId)
                .lenderId(1L)
                .amount(new BigDecimal(10000).setScale(2, RoundingMode.HALF_EVEN))
                .termsInMonths(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
        LoanInvestment loanInvestment2 = new LoanInvestment.Builder()
                //.loanInvestmentId(2L)
                .loanId(loanId)
                .lenderId(2L)
                .amount(new BigDecimal(50000).setScale(2, RoundingMode.HALF_EVEN))
                .termsInMonths(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        loanInvestmentList.add(loanInvestment1);
        loanInvestmentList.add(loanInvestment2);

        return loanInvestmentList;
    }



    private ProductType getProductType(Date createdOn){
        return new ProductType.Builder()
                //.productTypeId(1L)
                .productTypeCode("FRY2")
                .productTypeName("2 Fixed Rate Years")
                .description("Fixed rate for 2 years")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

    private BorrowerType getBorrowerType(Date createdOn){
        return new BorrowerType.Builder()
                //.borrowerTypeId(1L)
                .borrowerTypeCode("IND")
                .borrowerTypeName("Individual")
                .description("Individual Owner")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

    private PropertyType getPropertyType(Date createdOn){
        return new PropertyType.Builder()
                //.propertyTypeId(1L)
                .propertyTypeCode("STA")
                .propertyTypeName("Standard")
                .description("Standard Property")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }


    private Product getProduct(Long productTypeId, Long propertyTypeId, Date createdOn){
        return new Product.Builder()
                //.productId(1L)
                .productCode("STANDARD_FRY2_75")
                .productName("Standard 2 Year Fixed, 75 LTV")
                .tenure(25)
                .productTypeId(productTypeId)
                .propertyTypeId(propertyTypeId)
                .nationalityTypeId(1L)
                .fixedBorrowingRate(new BigDecimal(3.09))
                .trackerBorrowingRate(new BigDecimal(4.75))
                .productLtv(75)
                .productFee(new BigDecimal(1.75))
                .description("Standard 2 Year Fixed Rate for 75 LTV")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

    private RentalCoverage getRentalCoverage(Long productTypeId, Long borrowerTypeId, Long propertyTypeId, Date createdOn){
        return new RentalCoverage.Builder()
                //.rentalCoverageId(1L)
                .productTypeId(productTypeId)
                .borrowerTypeId(borrowerTypeId)
                .propertyTypeId(propertyTypeId)
                .rentalCoverage(140)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

}