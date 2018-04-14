package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Product;
import com.landbay.mortgage.entity.ProductType;
import com.landbay.mortgage.repository.ProductRepository;
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
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Transactional(readOnly=false)
    public Optional<ProductType> create(ProductType productType) {
        return Optional.ofNullable(productTypeRepository.save(productType));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        productTypeRepository.deleteAll();
    }


    @Transactional(readOnly=true)
    public Optional<ProductType> findByProductTypeId(Long id) {
        return productTypeRepository.findByProductTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<ProductType> findByCode(String code) {
        return productTypeRepository.findByProductTypeCodeAllIgnoringCase(code);
    }


}
