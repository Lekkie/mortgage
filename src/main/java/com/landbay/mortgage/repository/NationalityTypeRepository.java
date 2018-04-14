package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.BorrowerType;
import com.landbay.mortgage.entity.NationalityType;
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
public interface NationalityTypeRepository extends JpaRepository<NationalityType, Long> {

    Optional<NationalityType> findByNationalityTypeId(@Param("nationalityTypeId") Long nationalityTypeId);
    Optional<NationalityType> findByNationalityTypeCodeAllIgnoringCase(@Param("nationalityTypeCode") String nationalityTypeCode);



}
