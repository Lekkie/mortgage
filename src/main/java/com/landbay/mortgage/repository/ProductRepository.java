package com.landbay.mortgage.repository;

import com.landbay.mortgage.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(@Param("productId") Long productId);
    Optional<Product> findByProductCodeAllIgnoringCase(@Param("productCode") String productCode);

    /*
    @Query("FROM Product p WHERE p.productTypeId = :productTypeId " +
            "AND (p.propertyTypeId = :propertyTypeId OR p.propertyTypeId = 0)" +
            "AND p.nationalityTypeId = :nationalityTypeId " +
            "AND p.productLtv >= :productLtv")
    Optional<List<Product>> findByProductTypeIdPropertyTypeIdNationalityTypeIdLtvAllIgnoringCase(@Param("productTypeId") Long productTypeId, @Param("propertyTypeId") Long propertyTypeId, @Param("nationalityTypeId") Long nationalityTypeId, @Param("productLtv") BigDecimal productLtv);
    */

}
