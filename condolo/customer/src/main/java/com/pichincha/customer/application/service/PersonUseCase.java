package com.pichincha.customer.application.service;

import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.application.exception.SystemException;
import com.pichincha.customer.application.input.port.PersonService;
import com.pichincha.customer.application.output.port.PersonOutPort;
import com.pichincha.customer.domain.Person;
import com.pichincha.customer.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonUseCase implements PersonService {
    
    private final PersonOutPort personOutPort;

    @Override
    @Transactional
    public Person create(Person person) {
        if (person == null) {
            throw new ValidationException("Person cannot be null");
        }
        
        try {
            this.validateUniqueIdentification(person.getIdentification(), null);
            return personOutPort.save(person);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating person", e);
            throw new SystemException("Error creating person", e);
        }
    }

    @Override
    @Transactional
    public Person update(Person personToUpdate) {
        if (personToUpdate == null || personToUpdate.getPersonId() == null) {
            throw new ValidationException("Person and PersonId cannot be null");
        }
        
        try {
            Person person = this.findById(personToUpdate.getPersonId());
            String previousIdentification = person.getIdentification();

            BeanUtils.copyProperties(personToUpdate, person);
            
            this.validateUniqueIdentification(person.getIdentification(), previousIdentification);
            return personOutPort.save(person);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating person: {}", personToUpdate.getPersonId(), e);
            throw new SystemException("Error updating person", e);
        }
    }

    @Override
    public Collection<Person> findAll() {
        return personOutPort.findAll();
    }

    @Override
    public Person findById(String id) {
        return personOutPort.findById(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Person ID cannot be null or empty");
        }
        
        try {
            this.findById(id);
            this.personOutPort.delete(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting person: {}", id, e);
            throw new SystemException("Error deleting person", e);
        }
    }

    @Override
    public Person findByIdentification(String identification, String identificationUpdated) {
        return  personOutPort.findByIdentification(identification, identificationUpdated);
    }

    private void validateUniqueIdentification(String identification, String identificationUpdated) {
        Person existingPerson = personOutPort.findByIdentification(
                identification, identificationUpdated);
        if (existingPerson != null) {
            throw new ValidationException("The identification is already in use.");
        }
    }
}
