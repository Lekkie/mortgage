package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.RentalCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface RentalCoverageRepository extends JpaRepository<RentalCoverage, Long> {

    Optional<RentalCoverage> findByRentalCoverageId(@Param("rentalCoverageId") Long rentalCoverageId);
    @Query("FROM RentalCoverage rc WHERE rc.productTypeId = :productTypeId " +
            "AND rc.borrowerTypeId = :borrowerTypeId " +
            "AND rc.propertyTypeId = :propertyTypeId")
    Optional<RentalCoverage> findByProductTypeIdBorrowerTypeIdPropertyTypeId(@Param("productTypeId") Long productTypeId, @Param("borrowerTypeId") Long borrowerTypeId, @Param("propertyTypeId") Long propertyTypeId);



}
