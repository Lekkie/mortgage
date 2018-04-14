package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.BorrowerType;
import com.landbay.mortgage.entity.ProductType;
import com.landbay.mortgage.repository.BorrowerTypeRepository;
import com.landbay.mortgage.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class BorrowerTypeService {

    @Autowired
    private BorrowerTypeRepository borrowerTypeRepository;


    @Transactional(readOnly=false)
    public Optional<BorrowerType> create(BorrowerType borrowerType) {
        return Optional.ofNullable(borrowerTypeRepository.save(borrowerType));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        borrowerTypeRepository.deleteAll();
    }

    @Transactional(readOnly=true)
    public Optional<BorrowerType> findByBorrowerTypeId(Long id) {
        return borrowerTypeRepository.findByBorrowerTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<BorrowerType> findByCode(String code) {
        return borrowerTypeRepository.findByBorrowerTypeCodeAllIgnoringCase(code);
    }


}
