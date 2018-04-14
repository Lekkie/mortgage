package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Borrower;
import com.landbay.mortgage.entity.Loan;
import com.landbay.mortgage.repository.BorrowerRepository;
import com.landbay.mortgage.repository.LoanRepository;
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
public class BorrowerService {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Transactional(readOnly=false)
    public Optional<Borrower> create(Borrower borrower) {
        return Optional.ofNullable(borrowerRepository.save(borrower));
    }

    @Transactional(readOnly=false)
    public void delete(Long id) {
        borrowerRepository.delete(id);
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        borrowerRepository.deleteAll();
    }

    @Transactional(readOnly=true)
    public Optional<Borrower> findByBorrowerId(Long id) {
        return borrowerRepository.findByBorrowerId(id);
    }


    @Transactional(readOnly=true)
    public Optional<List<Borrower>> findAll() {
        return Optional.ofNullable(borrowerRepository.findAll());
    }


}
