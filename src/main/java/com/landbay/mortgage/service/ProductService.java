package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Product;
import com.landbay.mortgage.exceptions.EntityNotFoundException;
import com.landbay.mortgage.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Transactional(readOnly=false)
    public Optional<Product> create(Product product) {
        return Optional.ofNullable(productRepository.save(product));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        productRepository.deleteAll();
    }


    @Transactional(readOnly=true)
    public Optional<Product> findByProductId(Long id) {
        return productRepository.findByProductId(id);
    }

    @Transactional(readOnly=true)
    public Optional<Product> findByCode(String code) {
        return productRepository.findByProductCodeAllIgnoringCase(code);
    }



    /*
    @Transactional(readOnly=true)
    public Optional<List<Product>> findByProductTypeIdPropertyTypeIdNationalityTypeIdLtv(long productTypeId, long propertyTypeId, long nationalityTypeId, BigDecimal ltv) {
        return productRepository.findByProductTypeIdPropertyTypeIdNationalityTypeIdLtvAllIgnoringCase(productTypeId, propertyTypeId, nationalityTypeId, ltv);
    }

    @Transactional(readOnly=true)
    public Optional<Product> findBestMatchByProductTypeIdPropertyTypeIdNationalityTypeIdLtv(long productTypeId, long propertyTypeId, long nationalityTypeId, BigDecimal ltv) {
        List<Product> productList = productRepository.findByProductTypeIdPropertyTypeIdNationalityTypeIdLtvAllIgnoringCase(productTypeId, propertyTypeId, nationalityTypeId, ltv).orElseThrow(() -> new EntityNotFoundException("Product"));
        Product product = productList.stream()
                .max((a, b)  -> Product.BY_LTV.compare(a, b))
                .get();
        return Optional.ofNullable(product);
    }
    */


}
