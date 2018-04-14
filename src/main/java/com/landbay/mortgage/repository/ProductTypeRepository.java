package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.ProductType;
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
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findByProductTypeId(@Param("productTypeId") Long productTypeId);
    Optional<ProductType> findByProductTypeCodeAllIgnoringCase(@Param("productCode") String productTypeCode);


}
