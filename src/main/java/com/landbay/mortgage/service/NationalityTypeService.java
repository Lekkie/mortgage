package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.BorrowerType;
import com.landbay.mortgage.entity.NationalityType;
import com.landbay.mortgage.repository.BorrowerTypeRepository;
import com.landbay.mortgage.repository.NationalityTypeRepository;
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
public class NationalityTypeService {

    @Autowired
    private NationalityTypeRepository nationalityTypeRepository;


    @Transactional(readOnly=true)
    public Optional<NationalityType> findByNationalityTypeId(Long id) {
        return nationalityTypeRepository.findByNationalityTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<NationalityType> findByCode(String code) {
        return nationalityTypeRepository.findByNationalityTypeCodeAllIgnoringCase(code);
    }


}
