package com.bayzdelivery.validators;


import com.bayzdelivery.constants.PersonType;
import com.bayzdelivery.dtos.PersonDTO;
import com.bayzdelivery.model.Person;
import com.bayzdelivery.repositories.PersonRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PersonValidator {

    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validateDeliveryManAndCustomer(Long deliveryManId, Long customerId) {
        validateDeliveryMan(deliveryManId);
        validateCustomer(customerId);
    }

    public void validateDeliveryMan(Long deliveryManId) {

        Person deliveryMan = personRepository.findById(deliveryManId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery man ID: " + deliveryManId));
        
        if (!PersonType.DELIVERY_MAN.toString().equalsIgnoreCase(deliveryMan.getType().toString())) {
            throw new IllegalArgumentException("Person with ID " + deliveryManId + " is not a delivery man.");
        }

    }

    public void validateCustomer(Long customerId) {

        
        Person customer = personRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID: " + customerId));
        
        if (!PersonType.CUSTOMER.toString().equalsIgnoreCase(customer.getType().toString())) {
            throw new IllegalArgumentException("Person with ID " + customerId + " is not a customer.");
        }

    }


    public void checkIfPersonValid(PersonDTO personDTO) {
        if (personDTO == null) {
            throw new IllegalArgumentException("Person details cannot be null");
        }
        
        if (StringUtils.isBlank(personDTO.getEmail())) {
            throw new IllegalArgumentException("Please provide a valid email");
        }
        
        if (StringUtils.isBlank(personDTO.getName())) {
            throw new IllegalArgumentException("Please provide a valid name");
        }
        
        if (StringUtils.isBlank(personDTO.getRegistrationNumber())) {
            throw new IllegalArgumentException("Please provide a valid registration number");
        }
    }
}
