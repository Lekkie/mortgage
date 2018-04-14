package com.landbay.mortgage.controller;

import com.landbay.mortgage.config.EnvironmentDataConfig;
import com.landbay.mortgage.dto.LoanInfo;
import com.landbay.mortgage.dto.LoanRequest;
import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.entity.*;
import com.landbay.mortgage.exceptions.EntityNotFoundException;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.LoanApplicationFailureException;
import com.landbay.mortgage.exceptions.LoanRuntimeException;
import com.landbay.mortgage.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/loans", produces = "application/json")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanInvestmentService loanInvestmentService;
    @Autowired
    private InterestService interestService;
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


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity create(@RequestBody LoanRequest loanRequest)
    {
        Optional<LoanRequest> optionalAcq = Optional.ofNullable(loanRequest);
        final LoanRequest loanRequestFinal = optionalAcq.orElseThrow(() -> new IllegalMortgageArgumentException("Missing Loan request data"));
        loanRequestFinal.validateRequest();

        // get product type
        Product product = productService.findByCode(loanRequestFinal.getProductCode()).orElseThrow(() -> new EntityNotFoundException("Product " + loanRequestFinal.getProductCode()));
        Long productTypeId = product.getProductTypeId();
        // get application type
        ProductType productType = productTypeService.findByProductTypeId(productTypeId).orElseThrow(() -> new EntityNotFoundException("ProductType " + productTypeId));
        // get borrower type type
        BorrowerType borrowerType = borrowerTypeService.findByCode(loanRequestFinal.getBorrowerTypeCode()).orElseThrow(() -> new EntityNotFoundException("BorrowerType " + loanRequestFinal.getBorrowerTypeCode()));
        // get property type
        PropertyType propertyType = propertyTypeService.findByCode(loanRequestFinal.getPropertyTypeCode()).orElseThrow(() -> new EntityNotFoundException("PropertyType " + loanRequestFinal.getPropertyTypeCode()));
        Long propertyTypeId = propertyType.getPropertyTypeId();
          // get rental coverage
        RentalCoverage rentalCoverage = rentalCoverageService.findByProductTypeIdBorrowerTypeIdPropertyTypeId(productTypeId, borrowerType.getBorrowerTypeId(), propertyTypeId).orElseThrow(() -> new EntityNotFoundException("RentalCoverage"));

        // builder pattern
        Borrower borrower = new Borrower.Builder()
                .borrowerTypeId(borrowerType.getBorrowerTypeId())
                .firstname(loanRequestFinal.getBorrowerFirstname())
                .lastname(loanRequestFinal.getBorrowerLastname())
                .salary(loanRequestFinal.getBorrowerSalary())
                .nationality(loanRequestFinal.getBorrowerNationality())
                .age(loanRequestFinal.getBorrowerAge())
                .firstTimeLandlord(loanRequestFinal.isBorrowerFirstTimeLandlord())
                .firstTimeBuyer(loanRequestFinal.isBorrowerFirstTimeBuyer())
                .activeBankAccount(loanRequestFinal.isBorrowerActiveBankAccount())
                .ukCreditHistory(loanRequestFinal.isBorrowerUkCreditHistory())
                .build();
        borrower = borrowerService.create(borrower).orElseThrow(() -> new LoanApplicationFailureException("Unable to register borrower, please contact support."));

        // builder pattern
        double propertyValue = loanRequestFinal.getPropertyValue();
        Property property = new Property.Builder()
                .propertyTypeId(propertyTypeId)
                .postcode(loanRequestFinal.getPropertyPostcode())
                .county(loanRequestFinal.getPropertyCounty())
                .beds(loanRequestFinal.getPropertyBeds())
                .propertyValue(propertyValue)
                .monthlyRent(loanRequestFinal.getPropertyMonthlyRent())
                .rentalCoverage(rentalCoverage.getRentalCoverage())
                .build();
        property = propertyService.create(property).orElseThrow(() -> new LoanApplicationFailureException("Unable to register borrower's property, please contact support."));

        int tenureMonths = loanRequestFinal.getLoanTenureInMonths();
        LocalDate futureDate = LocalDate.now(ZoneId.systemDefault()).plusMonths(tenureMonths);
        Date completionDate = Date.from(LocalDateTime.of(futureDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());
        String productTypeCode = productType.getProductTypeCode();

        BigDecimal borrowerRate = "TRA".equalsIgnoreCase(productTypeCode) ? product.getTrackerBorrowingRate() : product.getFixedBorrowingRate();
        borrowerRate = borrowerRate.add(new BigDecimal(environmentDataConfig.getPlatformFeeRate()));
        double ltv = product.getProductLtv();
        // calculate loan amount from ltv
        BigDecimal loanAmount = loanService.getLoanAmount(propertyValue, ltv);
        BigDecimal downPayment = new BigDecimal(propertyValue).subtract(loanAmount);
        BigDecimal lenderRate = "TRA".equalsIgnoreCase(productTypeCode) ? new BigDecimal(environmentDataConfig.getLenderTrackerRate() + environmentDataConfig.getLiborRate()) : new BigDecimal(environmentDataConfig.getLenderFixedRate());
        // use builder pattern
        Loan loan = new Loan.Builder()
                .borrowerId(borrower.getBorrowerId())
                .propertyId(property.getPropertyId())
                .productId(product.getProductId())
                .purpose(loanRequestFinal.getLoanPurpose())
                .loanAmount(loanAmount)
                .downPayment(downPayment)
                .borrowerRate(borrowerRate)
                .lenderRate(lenderRate)
                .ltv(product.getProductLtv())
                .tenureInMonths(loanRequestFinal.getLoanTenureInMonths())
                .completionDate(completionDate)
                .investmentOpen(true)
                .build();

        // create loan
        loan = loanService.create(loan).orElseThrow(() -> new LoanApplicationFailureException("Unable to create loan application, please contact support."));

        BigDecimal monthlyRepayment = interestService.getMonthlyPaymentAmount(loan.getLoanAmount(), loan.getBorrowerRate(), loan.getTenureInMonths());
        LoanInfo loanInfo = new LoanInfo.Builder()
                //.loanId(loan.getLoanId())
                .loanAmount(loan.getLoanAmount().doubleValue())
                .borrowerRate(borrowerRate.doubleValue())
                .ltv(loan.getLtv())
                .rentalCoverage(property.getRentalCoverage())
                .monthlyRepayment(monthlyRepayment.doubleValue())
                .build();

        return new ResponseEntity<Object>(loanInfo, HttpStatus.CREATED);
    }




    @RequestMapping(method= RequestMethod.DELETE, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new IllegalMortgageArgumentException("Loan Id " + finalId));

        // You cant delete a loan with investments
        if(!loanInvestmentService.findByLoanId(id).isPresent()){
            Loan loan = loanService.findByLoanId(id).orElseThrow(() -> new EntityNotFoundException("Loan " + finalId));
            propertyService.delete(loan.getPropertyId());
            loanService.delete(id);
            return new ResponseEntity<Object>("", HttpStatus.OK);
        }

        Error error = new Error(2000, "Cannot delete loan with investments");
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") long id)
    {
        String fxnParams = "id=" + id ;
        final long finalId = id;
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new IllegalMortgageArgumentException("Loan Id " + finalId));

        Loan loan = loanService.findByLoanId(id).orElseThrow(() -> new EntityNotFoundException("Loan " + finalId));
        Property property = propertyService.findByPropertyId(loan.getPropertyId()).orElseThrow(() -> new EntityNotFoundException("Property " + loan.getPropertyId()));
        PropertyType propertyType = propertyTypeService.findByPropertyTypeId(property.getPropertyTypeId()).orElseThrow(() -> new EntityNotFoundException("Property Type " + property.getPropertyTypeId()));
        Borrower borrower = borrowerService.findByBorrowerId(loan.getBorrowerId()).orElseThrow(() -> new EntityNotFoundException("Borrower " + loan.getBorrowerId()));
        BorrowerType borrowerType = borrowerTypeService.findByBorrowerTypeId(borrower.getBorrowerTypeId()).orElseThrow(() -> new EntityNotFoundException("Borrower Type" + borrower.getBorrowerTypeId()));
        Product product = productService.findByProductId(loan.getProductId()).orElseThrow(() -> new EntityNotFoundException("LoanProduct " + loan.getProductId()));
        ProductType productType = productTypeService.findByProductTypeId(product.getProductTypeId()).orElseThrow(() -> new EntityNotFoundException("LoanProductType " + product.getProductTypeId()));

        BigDecimal borrowerRate = loan.getBorrowerRate();
        BigDecimal monthlyRepayment = interestService.getMonthlyPaymentAmount(loan.getLoanAmount(), borrowerRate, loan.getTenureInMonths());
        List<LoanInvestment> loanInvestmentList = loanInvestmentService.findByLoanId(id).orElse(new ArrayList());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // use builder pattern
        LoanInfo loanInfo = new LoanInfo.Builder()
                //.loanId(loan.getLoanId())
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
                .borrowerRate(borrowerRate.doubleValue())
                .completionDate(df.format(loan.getCompletionDate()))
                .tenureInMonths(loan.getTenureInMonths())
                .purpose(loan.getPurpose())
                .ltv(loan.getLtv())
                .lenderRate(loan.getLenderRate().doubleValue())
                .loanAmount(loan.getLoanAmount().doubleValue())
                .monthlyRepayment(monthlyRepayment.doubleValue())
                .investments(loanInvestmentList)
                .build();

        ResponseEntity responseEntity = new ResponseEntity<Object>(loanInfo, HttpStatus.OK);
        return responseEntity;
    }


    @ExceptionHandler(LoanRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Error handleRuntimeException(HttpServletRequest httpServletRequest, LoanRuntimeException lrex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        lrex.printStackTrace();
        return new Error(lrex.getErrorCode(), lrex.getMessage());
    }



}
