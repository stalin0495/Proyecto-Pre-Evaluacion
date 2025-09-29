package com.pichincha.customer.infrastructure.output.repository.mapper;

import com.pichincha.customer.infrastructure.output.repository.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface PersonMapper {

    com.pichincha.customer.domain.Person convertToDomain(Person entity);

    Person convertToEntity(com.pichincha.customer.domain.Person domain);
}
