package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import com.landbay.mortgage.repository.LoanInvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class LoanInvestmentService {

    @Autowired
    private LoanInvestmentRepository loanInvestmentRepository;

    @Transactional(readOnly=false)
    public Optional<LoanInvestment> create(LoanInvestment loanInvestment) {
        return Optional.ofNullable(loanInvestmentRepository.save(loanInvestment));
    }

    @Transactional(readOnly=false)
    public void delete(Long id) {
        loanInvestmentRepository.delete(id);
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        loanInvestmentRepository.deleteAll();
    }


    @Transactional(readOnly=true)
    public Optional<List<LoanInvestment>> findByLoanId(Long id) {
        return loanInvestmentRepository.findByLoanId(id);
    }

    @Transactional(readOnly=true)
    public Optional<List<LoanInvestment>> findByLenderId(Long id) {
        return loanInvestmentRepository.findByLenderId(id);
    }



}
