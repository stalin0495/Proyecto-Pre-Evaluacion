package com.pichincha.customer.application.output.port;

import com.pichincha.customer.domain.Person;

import java.util.Collection;

public interface PersonOutPort {

    Person save(Person person);

    Collection<Person> findAll();

    Person findById(String id);

    Person findByIdentification(String identification, String identificationUpdate);

    void delete(String id);
}
