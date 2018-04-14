package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.BorrowerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface BorrowerTypeRepository extends JpaRepository<BorrowerType, Long> {

    Optional<BorrowerType> findByBorrowerTypeId(@Param("borrowerTypeId") Long borrowerTypeId);
    Optional<BorrowerType> findByBorrowerTypeCodeAllIgnoringCase(@Param("borrowerTypeCode") String borrowerTypeCode);



}
