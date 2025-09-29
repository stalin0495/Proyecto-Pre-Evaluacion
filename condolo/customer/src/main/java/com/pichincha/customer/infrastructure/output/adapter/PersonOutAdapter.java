package com.pichincha.customer.infrastructure.output.adapter;

import com.pichincha.customer.application.output.port.PersonOutPort;
import com.pichincha.customer.domain.Person;
import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.infrastructure.output.repository.PersonRepository;
import com.pichincha.customer.infrastructure.output.repository.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PersonOutAdapter implements PersonOutPort {

    private final PersonMapper personMapper;

    private final PersonRepository personRepository;

    @Override
    public Person save(Person person) {
        return personMapper.convertToDomain(
                personRepository.save(
                        personMapper.convertToEntity(person)
                )
        );
    }
    @Override
    public Collection<Person> findAll() {
        return personRepository.findAll().stream().map(personMapper::convertToDomain).toList();
    }

    @Override
    public Person findById(String id) {
        return personMapper.convertToDomain(
                personRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Person not found")
                        ));
    }

    @Override
    public Person findByIdentification(String identification, String identificationUpdate) {
        return personMapper.convertToDomain(
                personRepository.findByIdentification(identification, identificationUpdate)
                        .orElseThrow(() -> new ResourceNotFoundException("Person not found")
                        ));
    }

    @Override
    public void delete(String id) {
        personRepository.deleteById(id);
    }
}
