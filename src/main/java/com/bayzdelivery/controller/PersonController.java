package com.bayzdelivery.controller;

import com.bayzdelivery.dtos.PersonDTO;
import com.bayzdelivery.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Register a new person
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PersonDTO personDTO) {
        if (personDTO == null || personDTO.getType() == null) {
            return ResponseEntity.badRequest().body("User must have a type: CUSTOMER or DELIVERY_MAN");
        }

        try {
            PersonDTO savedPerson = personService.save(personDTO);
            return ResponseEntity.ok(savedPerson);

        } catch (IllegalArgumentException e) {
            // Handles missing required fields or duplicate unique values
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (DataIntegrityViolationException e) {
            // Handles database constraint violations (like unique key failure)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email or Registration Number already exists.");

        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while processing the request.");
        }
    }

    /**
     * Get all persons
     */
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        return ResponseEntity.ok(personService.getAll());
    }

    /**
     * Get person by ID
     */
   @GetMapping("/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable Long personId) {
        try {
            PersonDTO person = personService.findById(personId);
            return ResponseEntity.ok(person);

        } catch (ResourceNotFoundException e) {
            // Handles cases where the person is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the person details.");
        }
    }
}
