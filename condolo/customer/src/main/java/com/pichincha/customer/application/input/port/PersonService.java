package com.pichincha.customer.application.input.port;

import com.pichincha.customer.domain.Person;

import java.util.Collection;

public interface PersonService {

    Person create(Person person);

    Person update(Person person);

    Collection<Person> findAll();

    Person findById(String id);

    void delete(String id);

    Person findByIdentification(String identification, String identificationUpdated);
}
