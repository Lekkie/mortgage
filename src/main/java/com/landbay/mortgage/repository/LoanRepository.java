package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByLoanId(@Param("loanId") Long loanId);
    @Query("FROM Loan l WHERE l.investmentOpen = :investmentOpen")
    Optional<List<Loan>> findByInvestmentOpen(@Param("investmentOpen") boolean investmentOpen);



}
