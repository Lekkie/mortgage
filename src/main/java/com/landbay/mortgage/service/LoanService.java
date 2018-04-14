package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Transactional(readOnly=false)
    public Optional<Loan> create(Loan loan) {
        return Optional.ofNullable(loanRepository.save(loan));
    }

    @Transactional(readOnly=false)
    public void delete(Long id) {
        loanRepository.delete(id);
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        loanRepository.deleteAll();
    }

    @Transactional(readOnly=true)
    public Optional<Loan> findByLoanId(Long id) {
        return loanRepository.findByLoanId(id);
    }

    @Transactional(readOnly=true)
    public Optional<List<Loan>> findAllOpenLoan() {
        return loanRepository.findByInvestmentOpen(true);
    }


    public BigDecimal getLoanAmount(double assetValue, double ltv){
        BigDecimal ltvRatioBigDecimal = new BigDecimal(ltv).setScale(12).divide(new BigDecimal(100), RoundingMode.HALF_EVEN);
        BigDecimal loanAmount = new BigDecimal(assetValue).setScale(12).multiply(ltvRatioBigDecimal);
        return loanAmount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
