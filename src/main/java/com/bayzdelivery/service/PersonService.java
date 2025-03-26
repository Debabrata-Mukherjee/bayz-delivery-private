package com.bayzdelivery.service;

import java.util.List;

import com.bayzdelivery.dtos.PersonDTO;

public interface PersonService {
  public List<PersonDTO> getAll();

  public PersonDTO save(PersonDTO personDTO);

  public PersonDTO findById(Long personId);

}
