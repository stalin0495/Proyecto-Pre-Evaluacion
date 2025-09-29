package com.pichincha.customer.infrastructure.output.repository;

import com.pichincha.customer.infrastructure.output.repository.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    @Query("SELECT p FROM Person p " +
            "WHERE (:identificationUpdate IS NULL OR p.identification <> :identificationUpdate) " +
            "AND p.identification = :identification")
    Optional<Person> findByIdentification(String identification, String identificationUpdate);
}