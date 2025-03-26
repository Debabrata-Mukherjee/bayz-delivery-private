package com.bayzdelivery.dtos;

import com.bayzdelivery.constants.PersonType;
import com.bayzdelivery.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("type")
    private PersonType type;

    @JsonProperty("registration_number")
    private String registrationNumber;

    public static PersonDTO fromEntity(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .email(person.getEmail())
                .type(person.getType())
                .registrationNumber(person.getRegistrationNumber())
                .build();
    }

    public Person toEntity() {
        Person person = new Person();
        person.setId(this.id);
        person.setName(this.name);
        person.setEmail(this.email);
        person.setType(type);
        person.setRegistrationNumber(registrationNumber);
        return person;
    }
}