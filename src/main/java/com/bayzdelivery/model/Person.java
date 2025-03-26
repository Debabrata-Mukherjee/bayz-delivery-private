package com.bayzdelivery.model;

import java.io.Serializable;

import com.bayzdelivery.constants.PersonType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "person",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "email", name = "uq_person_email"),
           @UniqueConstraint(columnNames = "registration_number", name = "uq_person_registration_number")
       })
public class Person implements Serializable {

    private static final long serialVersionUID = 432154291451321L;

    public Person() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @NotNull
    @Email
    @Column(name = "email", unique = true)  
    String email;

    @Column(name = "registration_number", unique = true) 
    String registrationNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PersonType type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public PersonType getType() {
        return type;
    }

    public void setType(PersonType type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return email.equals(other.email) && registrationNumber.equals(other.registrationNumber);
    }

    @Override
    public int hashCode() {
        return email.hashCode() + registrationNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", email=" + email + ", registrationNumber=" + registrationNumber + "]";
    }
}
