package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.RentalCoverage;
import com.landbay.mortgage.repository.RentalCoverageRepository;
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
public class RentalCoverageService {

    @Autowired
    private RentalCoverageRepository rentalCoverageRepository;

    @Transactional(readOnly=false)
    public Optional<RentalCoverage> create(RentalCoverage rentalCoverage) {
        return Optional.ofNullable(rentalCoverageRepository.save(rentalCoverage));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        rentalCoverageRepository.deleteAll();
    }

    @Transactional(readOnly=true)
    public Optional<RentalCoverage> findByRentalConverageId(Long id) {
        return rentalCoverageRepository.findByRentalCoverageId(id);
    }

    @Transactional(readOnly=true)
    public Optional<RentalCoverage> findByProductTypeIdBorrowerTypeIdPropertyTypeId(Long productTypeId, Long borrowerTypeId, Long propertyTypeId) {
        return rentalCoverageRepository.findByProductTypeIdBorrowerTypeIdPropertyTypeId(productTypeId, borrowerTypeId, propertyTypeId);
    }


}
