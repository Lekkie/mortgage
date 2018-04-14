package com.landbay.mortgage.service;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.landbay.mortgage.entity.Property;
import com.landbay.mortgage.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Transactional(readOnly=false)
    public Optional<Property> create(Property loanSecurity) {
        return Optional.ofNullable(propertyRepository.save(loanSecurity));
    }

    @Transactional(readOnly=false)
    public void deleteAll() {
        propertyRepository.deleteAll();
    }

    @Transactional(readOnly=false)
    public void delete(Long id) {
        propertyRepository.delete(id);
    }

    @Transactional(readOnly=true)
    public Optional<Property> findByPropertyId(Long id) {
        return propertyRepository.findByPropertyId(id);
    }



}
