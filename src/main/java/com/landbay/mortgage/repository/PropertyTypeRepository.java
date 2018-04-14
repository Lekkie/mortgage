package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.PropertyType;
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
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {

    Optional<PropertyType> findByPropertyTypeId(@Param("propertyTypeId") Long propertyTypeId);
    Optional<PropertyType> findByPropertyTypeCodeAllIgnoringCase(@Param("propertyTypeCode") String propertyTypeCode);

    
}
