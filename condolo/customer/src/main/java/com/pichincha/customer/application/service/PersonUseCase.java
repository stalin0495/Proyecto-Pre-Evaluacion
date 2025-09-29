package com.pichincha.customer.application.service;

import com.pichincha.customer.application.input.port.PersonService;
import com.pichincha.customer.application.output.port.PersonOutPort;
import com.pichincha.customer.domain.Person;
import com.pichincha.customer.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PersonUseCase implements PersonService {
    
    private final PersonOutPort personOutPort;

    @Override
    public Person create(Person person) {
        this.validateUniqueIdentification(person.getIdentification(), null);
        return personOutPort.save(person);
    }

    @Override
    public Person update(Person personToUpdate) {
        Person person = this.findById(personToUpdate.getPersonId());
        String previousIdentification = person.getIdentification();
        BeanUtils.copyProperties(person, personToUpdate);
        this.validateUniqueIdentification(person.getIdentification(), previousIdentification);
        return personOutPort.save(person);
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
    public void delete(String id) {
        this.personOutPort.delete(id);
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
