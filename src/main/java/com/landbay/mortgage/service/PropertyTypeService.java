package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.BorrowerType;
import com.landbay.mortgage.entity.PropertyType;
import com.landbay.mortgage.repository.BorrowerTypeRepository;
import com.landbay.mortgage.repository.PropertyTypeRepository;
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
public class PropertyTypeService {

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;


    @Transactional(readOnly=false)
    public Optional<PropertyType> create(PropertyType propertyType) {
        return Optional.ofNullable(propertyTypeRepository.save(propertyType));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        propertyTypeRepository.deleteAll();
    }

    @Transactional(readOnly=true)
    public Optional<PropertyType> findByPropertyTypeId(Long id) {
        return propertyTypeRepository.findByPropertyTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<PropertyType> findByCode(String code) {
        return propertyTypeRepository.findByPropertyTypeCodeAllIgnoringCase(code);
    }


}
