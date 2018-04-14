package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.entity.LoanInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface LoanInvestmentRepository extends JpaRepository<LoanInvestment, Long> {

    Optional<List<LoanInvestment>> findByLoanId(@Param("loanId") Long loanId);
    Optional<List<LoanInvestment>> findByLenderId(@Param("lenderId") Long lenderId);


}
