package com.landbay.mortgage.service;

import com.landbay.mortgage.config.EnvironmentDataConfig;
import com.landbay.mortgage.dto.InvestmentRequest;
import com.landbay.mortgage.dto.LoanRequest;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lekanomotayo on 11/04/2018.
 */

@Service
public class RiskService {

    @Autowired
    LoanService loanService;
    @Autowired
    LoanInvestmentService loanInvestmentService;

    //A better strategy should be used in selecting loans. THis is for test purposes
    public Loan findSuitableLoan(InvestmentRequest investmentRequest){
        // get all unfilled loans
        List<Loan> loanList = loanService.findAllOpenLoan().orElse(new ArrayList<>());
        for(Loan loan: loanList){
            //match with investment request
            // check the amount to be invested is less than unfilled loan
            // get all loan investments and calculate amount invested so far
            List<LoanInvestment> loanInvestmentList = loanInvestmentService.findByLoanId(loan.getLoanId()).orElse(new ArrayList<>());
            BigDecimal loanInvestedAmtSum = loanInvestmentList
                    .stream()
                    .filter(lI -> lI != null && lI.getAmount() != null)
                    .map(LoanInvestment::getAmount)
                    .reduce((a, b) -> a.add(b))
                    .orElse(new BigDecimal(0));

            if(loan.getLoanAmount().doubleValue() > (loanInvestedAmtSum.doubleValue() + investmentRequest.getAmount().doubleValue()))
                return loan;
        }

        return new Loan();
    }

}
