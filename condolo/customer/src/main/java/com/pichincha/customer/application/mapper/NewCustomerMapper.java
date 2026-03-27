package com.pichincha.customer.application.mapper;

import com.pichincha.customer.domain.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper para actualizaciones parciales de Customer.
 * Nota: Los campos primitivos (age, status) requieren condiciones explícitas
 * ya que no pueden ser null y siempre tienen un valor por defecto.
 */
@Mapper(componentModel = "spring")
public interface NewCustomerMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "age", conditionExpression = "java(newCustomer.getAge() != 0)")
    @Mapping(target = "status", ignore = true)
    void updateCustomer(Customer newCustomer, @MappingTarget Customer customer);
}
