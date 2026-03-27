package com.pichincha.customer.infrastructure.output.adapter;

import com.pichincha.customer.application.exception.DatabaseException;
import com.pichincha.customer.application.output.port.PersonOutPort;
import com.pichincha.customer.domain.Person;
import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.infrastructure.output.repository.PersonRepository;
import com.pichincha.customer.infrastructure.output.repository.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonOutAdapter implements PersonOutPort {

    private final PersonMapper personMapper;

    private final PersonRepository personRepository;

    @Override
    public Person save(Person person) {
        try {
            return personMapper.convertToDomain(
                    personRepository.save(
                            personMapper.convertToEntity(person)
                    )
            );
        } catch (DataAccessException e) {
            log.error("Database error while saving person", e);
            throw new DatabaseException("Error saving person: " + e.getMessage(), e);
        }
    }
    @Override
    public Collection<Person> findAll() {
        try {
            return personRepository.findAll().stream()
                    .map(personMapper::convertToDomain)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Database error while finding all persons", e);
            throw new DatabaseException("Error retrieving persons: " + e.getMessage(), e);
        }
    }

    @Override
    public Person findById(String id) {
        try {
            return personMapper.convertToDomain(
                    personRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id)
                            ));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while finding person by id: {}", id, e);
            throw new DatabaseException("Error retrieving person: " + e.getMessage(), e);
        }
    }

    @Override
    public Person findByIdentification(String identification, String identificationUpdate) {
        try {
            return personMapper.convertToDomain(
                    personRepository.findByIdentification(identification, identificationUpdate)
                            .orElseThrow(() -> new ResourceNotFoundException("Person not found with identification: " + identification)
                            ));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while finding person by identification: {}", identification, e);
            throw new DatabaseException("Error retrieving person: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            personRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
            personRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while deleting person: {}", id, e);
            throw new DatabaseException("Error deleting person: " + e.getMessage(), e);
        }
    }
}
