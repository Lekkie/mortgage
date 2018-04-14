package com.landbay.mortgage.controller;

import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InterestInfo;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import com.landbay.mortgage.exceptions.EntityNotFoundException;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.IllegalMortgageStateException;
import com.landbay.mortgage.exceptions.LoanRuntimeException;
import com.landbay.mortgage.service.InterestService;
import com.landbay.mortgage.service.LoanInvestmentService;
import com.landbay.mortgage.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@RestController
@RequestMapping(value = "api/v1/lenders", produces = "application/json")
public class LenderController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private InterestService interestService;
    @Autowired
    private LoanInvestmentService loanInvestmentService;

    /***
     * This function takes the lender id and calculate interest owed to it in a month.
     * If a start and end date are supplied, it calculates the interest owed within the period in days.
     * @param id - Id of lender
     * @return
     */
    @RequestMapping(method= RequestMethod.GET, value = "/{id}/interests", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity getInterestByLenderId(@PathVariable("id") long id,
                                        @RequestParam(value="startDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                        @RequestParam(value="endDate", required = false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate)
    {
        String fxnParams = "id=" + id ;
        final long finalId = id;
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new IllegalMortgageArgumentException("Lender Id " + finalId));

        List<LoanInvestment> loanInvestmentList = loanInvestmentService.findByLenderId(id).orElseThrow(() -> new EntityNotFoundException("Loan Investment for Lender " + finalId));
        BigDecimal totalLenderInterest = new BigDecimal(0);

        for(LoanInvestment loanInvestment: loanInvestmentList){
            Loan loan = loanService.findByLoanId(loanInvestment.getLoanId()).orElseThrow(() -> new EntityNotFoundException("Loan Investment for Lender " + loanInvestment.getLoanId()));
            // Calculate How much has been paid
            if(Optional.ofNullable(loan).isPresent()){
                if(Optional.ofNullable(startDate).isPresent() && Optional.ofNullable(endDate).isPresent()){
                    //get interest by days
                    // default date format for startdate & end date: 2007-12-03
                    LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if(startLocalDate.isAfter(endLocalDate))
                        throw new IllegalMortgageStateException("Start date cannot be after end date");

                    //The number of days Interest will be calculated for
                    Long interestDays = DAYS.between(startLocalDate, endLocalDate);
                    BigDecimal interestAmount = interestService.getSimpleDailyInterest(loanInvestment.getAmount(), loan.getLenderRate(), interestDays.intValue());
                    totalLenderInterest = totalLenderInterest.add(interestAmount);
                }
                else{
                    BigDecimal interestAmount = interestService.getSimpleMonthlyInterest(loanInvestment.getAmount(), loan.getLenderRate(), loanInvestment.getTermsInMonths());
                    totalLenderInterest = totalLenderInterest.add(interestAmount);
                }
            }
        }

        InterestInfo interestInfo = new InterestInfo(totalLenderInterest.doubleValue());
        ResponseEntity responseEntity = new ResponseEntity<Object>(interestInfo, HttpStatus.OK);

        return responseEntity;
    }



    @ExceptionHandler(LoanRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Error handleRuntimeException(HttpServletRequest httpServletRequest, LoanRuntimeException lrex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        return new Error(lrex.getErrorCode(), lrex.getMessage());
    }





}
