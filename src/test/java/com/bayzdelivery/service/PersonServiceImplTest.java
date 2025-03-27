package com.bayzdelivery.service;


import com.bayzdelivery.constants.PersonType;
import com.bayzdelivery.dtos.PersonDTO;
import com.bayzdelivery.model.Person;
import com.bayzdelivery.repositories.PersonRepository;
import com.bayzdelivery.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonValidator personValidator;

    @InjectMocks
    private PersonServiceImpl personService;

    private PersonDTO personDTO;
    private Person person;

    @BeforeEach
    void setUp() {
        personDTO = new PersonDTO();
        personDTO.setId(1L);
        personDTO.setName("John Doe");
        personDTO.setEmail("john.doe@example.com");
        personDTO.setRegistrationNumber("12345");
        personDTO.setType(PersonType.CUSTOMER);

        person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setEmail("john.doe@example.com");
        person.setRegistrationNumber("12345");
        person.setType(PersonType.CUSTOMER);
    }

    @Test
    void testGetAllPersons() {
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<PersonDTO> persons = personService.getAll();
        assertFalse(persons.isEmpty());
        assertEquals(1, persons.size());
    }

    @Test
    void testSavePerson_Success() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        PersonDTO savedPerson = personService.save(personDTO);
        assertNotNull(savedPerson);
        assertEquals("John Doe", savedPerson.getName());
    }

    @Test
    void testSavePerson_FailsWithDuplicateEmail() {
        when(personRepository.save(any(Person.class))).thenThrow(new DataIntegrityViolationException("Unique constraint"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> personService.save(personDTO));
        assertEquals("Database error occurred while saving the user.", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        PersonDTO foundPerson = personService.findById(1L);
        assertNotNull(foundPerson);
        assertEquals("John Doe", foundPerson.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> personService.findById(1L));
        assertEquals("Person with ID 1 not found", exception.getMessage());
    }
}

