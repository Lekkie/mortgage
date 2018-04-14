package com.landbay.mortgage.controller;

import com.landbay.mortgage.dto.Error;
import com.landbay.mortgage.dto.InvestmentRequest;
import com.landbay.mortgage.entity.*;
import com.landbay.mortgage.exceptions.IllegalMortgageArgumentException;
import com.landbay.mortgage.exceptions.InvestmentApplicationFailureException;
import com.landbay.mortgage.exceptions.LoanApplicationFailureException;
import com.landbay.mortgage.exceptions.LoanRuntimeException;
import com.landbay.mortgage.service.LoanInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@RestController
@RequestMapping(value = "api/v1/investments", produces = "application/json")
public class InvestmentController {

    @Autowired
    private LoanInvestmentService loanInvestmentService;


    // Lender investment into a loan (create).
    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity create(@RequestBody InvestmentRequest investmentRequest)
    {
        investmentRequest = Optional.ofNullable(investmentRequest).orElseThrow(() -> new IllegalMortgageArgumentException("Missing InvestmentRequest request data"));
        investmentRequest.validateRequest();

        // use builder pattern
        LoanInvestment loanInvestment = new LoanInvestment.Builder()
                .lenderId(investmentRequest.getLenderId())
                .loanId(investmentRequest.getLoanId())
                .amount(investmentRequest.getAmount())
                .termsInMonths(investmentRequest.getTermInMonths())
                .build();

        // link loan to investment
        loanInvestment = loanInvestmentService.create(loanInvestment).orElseThrow(() -> new InvestmentApplicationFailureException("Unable to create investment application, please contact support."));
        loanInvestment.setLoanInvestmentId(null);

        return new ResponseEntity<LoanInvestment>(loanInvestment, HttpStatus.CREATED);
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
