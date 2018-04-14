package com.landbay.mortgage.controller;

import com.landbay.mortgage.config.EnvironmentDataConfig;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doReturn;

/**
 * Created by lekanomotayo on 13/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    private LoanController loanController;

    @Mock
    private BorrowerService borrowerService;
    @Mock
    private PropertyService propertyService;
    @Mock
    private ProductService productService;
    @Mock
    private LoanService loanService;
    @Mock
    private RentalCoverageService rentalCoverageService;
    @Mock
    private ProductTypeService productTypeService;
    @Mock
    private PropertyTypeService propertyTypeService;
    @Mock
    private BorrowerTypeService borrowerTypeService;
    @Mock
    private EnvironmentDataConfig environmentDataConfig;
    @Mock
    private LoanInvestmentService loanInvestmentService;

    InterestService interestService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void setup() throws Exception {
        loanController = new LoanController();
        interestService = new InterestService();
        TestUtils.setField(loanController, "borrowerService", borrowerService);
        TestUtils.setField(loanController, "propertyService", propertyService);
        TestUtils.setField(loanController, "productService", productService);
        TestUtils.setField(loanController, "rentalCoverageService", rentalCoverageService);
        TestUtils.setField(loanController, "productTypeService", productTypeService);
        TestUtils.setField(loanController, "propertyTypeService", propertyTypeService);
        TestUtils.setField(loanController, "borrowerTypeService", borrowerTypeService);
        TestUtils.setField(loanController, "environmentDataConfig", environmentDataConfig);
        TestUtils.setField(loanController, "interestService", interestService);
        TestUtils.setField(loanController, "loanInvestmentService", loanInvestmentService);
        TestUtils.setField(loanController, "loanService", loanService);
    }





    @Test
    public void shouldReturnLoanInfoResponseEntityForCreatedLoan() {

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49);
        ProductType productType = getProductType(createdOn);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);
        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(productService.findByCode(givenLoanRequest.getProductCode()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.ofNullable(productType));
        given(borrowerTypeService.findByCode(givenLoanRequest.getBorrowerTypeCode()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(propertyTypeService.findByCode(givenLoanRequest.getPropertyTypeCode()))
                .willReturn(Optional.ofNullable(propertyType));
        given(rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(product.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(rentalCoverage));
        given(borrowerService.create(Matchers.<Borrower>any()))
                .willReturn(Optional.ofNullable(borrower));
        given(propertyService.create(Matchers.<Property>any()))
                .willReturn(Optional.ofNullable(property));
        given(environmentDataConfig.getLiborRate())
                .willReturn(0.61);
        given(environmentDataConfig.getLenderTrackerRate())
                .willReturn(2.50);
        given(environmentDataConfig.getLenderFixedRate())
                .willReturn(lenderFixedRate.doubleValue());
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.getLoanAmount(property.getPropertyValue(), product.getProductLtv()))
                .willReturn(loanAmt);
        given(loanService.create(Matchers.<Loan>any()))
                .willReturn(Optional.ofNullable(loan));

        ResponseEntity responseEntity =  loanController.create(givenLoanRequest);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(LoanInfo.class));
        LoanInfo actual = (LoanInfo) body;

        LoanInfo expected = new LoanInfo.Builder()
                //.loanId(1L)
                .loanAmount(187500.0)
                .borrowerRate(4.09)
                .ltv(75.0)
                .rentalCoverage(140.0)
                .monthlyRepayment(997.96)
                .build();

        assertThat(actual, is(expected));
    }



    @Test
    public void shouldReturnIllegalMortgageArgumentExceptionForWrongTenureTerm() {

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 0;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        ProductType productType = getProductType(createdOn);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(productService.findByCode(givenLoanRequest.getProductCode()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.ofNullable(productType));
        given(borrowerTypeService.findByCode(givenLoanRequest.getBorrowerTypeCode()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(propertyTypeService.findByCode(givenLoanRequest.getPropertyTypeCode()))
                .willReturn(Optional.ofNullable(propertyType));
        given(rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(product.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(rentalCoverage));
        given(borrowerService.create(Matchers.<Borrower>any()))
                .willReturn(Optional.ofNullable(borrower));
        given(propertyService.create(Matchers.<Property>any()))
                .willReturn(Optional.ofNullable(property));
        given(environmentDataConfig.getLiborRate())
                .willReturn(0.61);
        given(environmentDataConfig.getLenderTrackerRate())
                .willReturn(2.50);
        given(environmentDataConfig.getLenderFixedRate())
                .willReturn(lenderFixedRate.doubleValue());
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.getLoanAmount(property.getPropertyValue(), product.getProductLtv()))
                .willReturn(loanAmt);
        given(loanService.create(Matchers.<Loan>any()))
                .willReturn(Optional.ofNullable(loan));

        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Loan tenure must be more than 1 month or less than 25 years (300 months)");
        ResponseEntity responseEntity =  loanController.create(givenLoanRequest);
    }




    @Test
    public void shouldReturnEntityNotFoundExceptionForWrongProductType() {

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(productService.findByCode(givenLoanRequest.getProductCode()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.empty());
        given(borrowerTypeService.findByCode(givenLoanRequest.getBorrowerTypeCode()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(propertyTypeService.findByCode(givenLoanRequest.getPropertyTypeCode()))
                .willReturn(Optional.ofNullable(propertyType));
        given(rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(product.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(rentalCoverage));
        given(borrowerService.create(Matchers.<Borrower>any()))
                .willReturn(Optional.ofNullable(borrower));
        given(propertyService.create(Matchers.<Property>any()))
                .willReturn(Optional.ofNullable(property));
        given(environmentDataConfig.getLiborRate())
                .willReturn(0.61);
        given(environmentDataConfig.getLenderTrackerRate())
                .willReturn(2.50);
        given(environmentDataConfig.getLenderFixedRate())
                .willReturn(lenderFixedRate.doubleValue());
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.getLoanAmount(property.getPropertyValue(), product.getProductLtv()))
                .willReturn(loanAmt);
        given(loanService.create(Matchers.<Loan>any()))
                .willReturn(Optional.ofNullable(loan));

        exception.expect(EntityNotFoundException.class);
        exception.expectMessage("ProductType 1 not found");
        ResponseEntity responseEntity =  loanController.create(givenLoanRequest);
    }



    @Test
    public void shouldReturnIllegalMortgageArgumentExceptionForWrongProductType() {

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(productService.findByCode(givenLoanRequest.getProductCode()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.empty());
        given(borrowerTypeService.findByCode(givenLoanRequest.getBorrowerTypeCode()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(propertyTypeService.findByCode(givenLoanRequest.getPropertyTypeCode()))
                .willReturn(Optional.ofNullable(propertyType));
        given(rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(product.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(rentalCoverage));
        given(borrowerService.create(Matchers.<Borrower>any()))
                .willReturn(Optional.ofNullable(borrower));
        given(propertyService.create(Matchers.<Property>any()))
                .willReturn(Optional.ofNullable(property));
        given(environmentDataConfig.getLiborRate())
                .willReturn(0.61);
        given(environmentDataConfig.getLenderTrackerRate())
                .willReturn(2.50);
        given(environmentDataConfig.getLenderFixedRate())
                .willReturn(lenderFixedRate.doubleValue());
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.getLoanAmount(property.getPropertyValue(), product.getProductLtv()))
                .willReturn(loanAmt);
        given(loanService.create(Matchers.<Loan>any()))
                .willReturn(Optional.ofNullable(loan));

        exception.expect(IllegalMortgageArgumentException.class);
        exception.expectMessage("Missing Loan request data");
        ResponseEntity responseEntity =  loanController.create(null);
    }



    @Test
    public void shouldReturnLoanApplicationFailureExceptionForWrongProductType() {

        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        ProductType productType = getProductType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(1L)
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();

        given(productService.findByCode(givenLoanRequest.getProductCode()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.ofNullable(productType));
        given(borrowerTypeService.findByCode(givenLoanRequest.getBorrowerTypeCode()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(propertyTypeService.findByCode(givenLoanRequest.getPropertyTypeCode()))
                .willReturn(Optional.ofNullable(propertyType));
        given(rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(product.getProductTypeId(), borrowerType.getBorrowerTypeId(), propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(rentalCoverage));
        given(borrowerService.create(Matchers.<Borrower>any()))
                .willReturn(Optional.empty());
        given(propertyService.create(Matchers.<Property>any()))
                .willReturn(Optional.ofNullable(property));
        given(environmentDataConfig.getLiborRate())
                .willReturn(0.61);
        given(environmentDataConfig.getLenderTrackerRate())
                .willReturn(2.50);
        given(environmentDataConfig.getLenderFixedRate())
                .willReturn(lenderFixedRate.doubleValue());
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.getLoanAmount(property.getPropertyValue(), product.getProductLtv()))
                .willReturn(loanAmt);
        given(loanService.create(Matchers.<Loan>any()))
                .willReturn(Optional.ofNullable(loan));

        exception.expect(LoanApplicationFailureException.class);
        exception.expectMessage("Unable to register borrower, please contact support.");
        ResponseEntity responseEntity =  loanController.create(givenLoanRequest);
    }






    @Test
    public void shouldReturnLoanInfoResponseEntityForGetLoanId() {

        Long loanId = 1L;
        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        ProductType productType = getProductType(createdOn);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(loanId)
                .borrowerId(1L)
                .propertyId(1L)
                .productId(1L)
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(loanId, 1L, createdOn);

        given(propertyService.findByPropertyId(loan.getPropertyId()))
                .willReturn(Optional.ofNullable(property));
        given(propertyTypeService.findByPropertyTypeId(propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(propertyType));
        given(borrowerService.findByBorrowerId(loan.getBorrowerId()))
                .willReturn(Optional.ofNullable(borrower));
        given(borrowerTypeService.findByBorrowerTypeId(borrower.getBorrowerTypeId()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(productService.findByProductId(loan.getProductId()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.ofNullable(productType));
        given(loanInvestmentService.findByLoanId(loan.getLoanId()))
                .willReturn(Optional.ofNullable(loanInvestmentList));
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.findByLoanId(loanId))
                .willReturn(Optional.ofNullable(loan));

        ResponseEntity responseEntity =  loanController.getById(loanId);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(LoanInfo.class));
        LoanInfo actual = (LoanInfo) body;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        LoanInfo expected = new LoanInfo.Builder()
                //.loanId(1L)
                .beds(property.getBeds())
                .monthlyRent(property.getMonthlyRent())
                .propertyValue(property.getPropertyValue())
                .county(property.getCounty())
                .postcode(property.getPostcode())
                .rentalCoverage(property.getRentalCoverage())
                .borrowerId(borrower.getBorrowerId())
                .borrowerType(borrowerType.getBorrowerTypeName())
                .productType(productType.getProductTypeName())
                .propertyType(propertyType.getPropertyTypeName())
                .product(product.getProductName())
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

        assertThat(actual, is(expected));
    }



    @Test
    public void shouldReturnEntityNotFoundExceptionForGetLoanId() {

        Long loanId = 1L;
        Date createdOn = new Date();
        BigDecimal loanAmt = new BigDecimal(187500.0).setScale(2, RoundingMode.HALF_EVEN);
        int tenureMonths = 300;
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal lenderFixedRate = new BigDecimal(3.49).setScale(2, RoundingMode.HALF_EVEN);
        ProductType productType = getProductType(createdOn);
        BorrowerType borrowerType = getBorrowerType(createdOn);
        PropertyType propertyType = getPropertyType(createdOn);
        Product product = getProduct(createdOn);
        RentalCoverage rentalCoverage = getRentalCoverage(createdOn);
        LoanRequest givenLoanRequest = getLoanRequest(tenureMonths);
        Borrower borrower = getBorrower(givenLoanRequest, borrowerType, createdOn);
        Property property = getProperty(givenLoanRequest, propertyType, rentalCoverage, createdOn);
        BigDecimal borrowerRate = product.getFixedBorrowingRate().add(new BigDecimal(1.0)).setScale(2, RoundingMode.HALF_EVEN);

        Loan loan = new Loan.Builder()
                .loanId(loanId)
                .borrowerId(1L)
                .propertyId(1L)
                .productId(1L)
                .purpose(givenLoanRequest.getLoanPurpose())
                .loanAmount(loanAmt)
                .downPayment(new BigDecimal(givenLoanRequest.getPropertyValue()).subtract(loanAmt))
                .borrowerRate(borrowerRate)
                .lenderRate(lenderFixedRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(givenLoanRequest.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .createdBy("User")
                .createdOn(createdOn)
                .build();
        List<LoanInvestment> loanInvestmentList = getLoanInvestments(loanId, 1L, createdOn);

        given(propertyService.findByPropertyId(loan.getPropertyId()))
                .willReturn(Optional.ofNullable(property));
        given(propertyTypeService.findByPropertyTypeId(propertyType.getPropertyTypeId()))
                .willReturn(Optional.ofNullable(propertyType));
        given(borrowerService.findByBorrowerId(loan.getBorrowerId()))
                .willReturn(Optional.ofNullable(borrower));
        given(borrowerTypeService.findByBorrowerTypeId(borrower.getBorrowerTypeId()))
                .willReturn(Optional.ofNullable(borrowerType));
        given(productService.findByProductId(loan.getProductId()))
                .willReturn(Optional.ofNullable(product));
        given(productTypeService.findByProductTypeId(product.getProductTypeId()))
                .willReturn(Optional.ofNullable(productType));
        given(loanInvestmentService.findByLoanId(loan.getLoanId()))
                .willReturn(Optional.ofNullable(loanInvestmentList));
        given(environmentDataConfig.getPlatformFeeRate())
                .willReturn(1.0);
        given(loanService.findByLoanId(loanId))
                .willReturn(Optional.empty());

        exception.expect(EntityNotFoundException.class);
        exception.expectMessage("Loan 1 not found");
        ResponseEntity responseEntity =  loanController.getById(loanId);
    }






    private LoanRequest getLoanRequest(int tenureMonths){
        return new LoanRequest.Builder()
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
                .loanTenureInMonths(tenureMonths)
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
    }

    private Borrower getBorrower(LoanRequest givenLoanRequest, BorrowerType borrowerType, Date createdOn){
        return new Borrower.Builder()
                .borrowerId(1L)
                .borrowerTypeId(borrowerType.getBorrowerTypeId())
                .firstname(givenLoanRequest.getBorrowerFirstname())
                .lastname(givenLoanRequest.getBorrowerLastname())
                .salary(givenLoanRequest.getBorrowerSalary())
                .nationality(givenLoanRequest.getBorrowerNationality())
                .age(givenLoanRequest.getBorrowerAge())
                .firstTimeLandlord(givenLoanRequest.isBorrowerFirstTimeLandlord())
                .firstTimeBuyer(givenLoanRequest.isBorrowerFirstTimeBuyer())
                .activeBankAccount(givenLoanRequest.isBorrowerActiveBankAccount())
                .ukCreditHistory(givenLoanRequest.isBorrowerUkCreditHistory())
                .createdBy("User")
                .createdOn(createdOn)
                .build();
    }

    private Property getProperty(LoanRequest givenLoanRequest, PropertyType propertyType, RentalCoverage rentalCoverage, Date createdOn){
        return new Property.Builder()
                .propertyId(1L)
                .propertyTypeId(propertyType.getPropertyTypeId())
                .postcode(givenLoanRequest.getPropertyPostcode())
                .county(givenLoanRequest.getPropertyCounty())
                .beds(givenLoanRequest.getPropertyBeds())
                .propertyValue(givenLoanRequest.getPropertyValue())
                .monthlyRent(givenLoanRequest.getPropertyMonthlyRent())
                .rentalCoverage(rentalCoverage.getRentalCoverage())
                .createdBy("User")
                .createdOn(createdOn)
                .build();
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
        LoanInvestment loanInvestment2 = new LoanInvestment.Builder()
                .loanInvestmentId(2L)
                .loanId(loanId)
                .lenderId(lenderId)
                .amount(new BigDecimal(50000))
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
                .productTypeId(1L)
                .productTypeCode("FRY2")
                .productTypeName("2 Fixed Rate Years")
                .description("Fixed rate for 2 years")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

    private BorrowerType getBorrowerType(Date createdOn){
        return new BorrowerType.Builder()
                .borrowerTypeId(1L)
                .borrowerTypeCode("IND")
                .borrowerTypeName("Individual")
                .description("Individual Owner")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }

    private PropertyType getPropertyType(Date createdOn){
        return new PropertyType.Builder()
                .propertyTypeId(1L)
                .propertyTypeCode("STA")
                .propertyTypeName("Standard")
                .description("Standard Property")
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }


    private Product getProduct(Date createdOn){
        return new Product.Builder()
                .productId(1L)
                .productCode("STANDARD_FRY2_75")
                .productName("Standard 2 Year Fixed, 75 LTV")
                .tenure(25)
                .productTypeId(1L)
                .propertyTypeId(1L)
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

    private RentalCoverage getRentalCoverage(Date createdOn){
        return new RentalCoverage.Builder()
                .rentalCoverageId(1L)
                .productTypeId(1L)
                .borrowerTypeId(1L)
                .propertyTypeId(1L)
                .rentalCoverage(140)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
    }
}