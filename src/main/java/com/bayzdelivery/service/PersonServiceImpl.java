package com.bayzdelivery.service;

import java.util.ArrayList;
import java.util.List;

import com.bayzdelivery.repositories.PersonRepository;
import com.bayzdelivery.validators.PersonValidator;
import com.bayzdelivery.dtos.PersonDTO;
import com.bayzdelivery.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonValidator personValidator;

    @Override
    public List<PersonDTO> getAll() {
        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(personList::add); 
        return personList.stream()
            .map(PersonDTO::fromEntity)
            .toList();
    }

    public PersonDTO save(PersonDTO p) {
    personValidator.checkIfPersonValid(p);
    // Convert DTO to Entity
    Person personEntity = p.toEntity();

    try {
        Person savedPerson = personRepository.save(personEntity);

        return PersonDTO.fromEntity(savedPerson);

    } catch (DataIntegrityViolationException e) {
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            throw new IllegalArgumentException("Email or Registration Number already exists.");
        }
        throw new RuntimeException("Database error occurred while saving the user.", e);

    } catch (TransactionSystemException e) {
        throw new RuntimeException("Invalid data format or missing required fields.", e);
    }
}

@Override
public PersonDTO findById(Long personId) {
    return personRepository.findById(personId)
            .map(PersonDTO::fromEntity)
            .orElseThrow(() -> new ResourceNotFoundException("Person with ID " + personId + " not found"));
}
}
