package com.pichincha.customer.application.service;

import com.pichincha.customer.application.output.port.PersonOutPort;
import com.pichincha.customer.application.exception.ValidationException;
import com.pichincha.customer.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

    @Mock
    private PersonOutPort personOutPort;

    @InjectMocks
    private PersonUseCase personUseCase;

    private Person samplePerson;

    @BeforeEach
    void setUp() {
        samplePerson = Person.builder()
                .personId("1")
                .name("Ana Martinez")
                .gender("Femenino")
                .age((short) 28)
                .identification("0987654321")
                .address("Av Republica 456")
                .phone("0998765432")
                .build();
    }

    @Test
    void testCreatePerson_Success() {
        Person personToCreate = Person.builder()
                .name("Carlos Rodriguez")
                .gender("Masculino")
                .age((short) 35)
                .identification("1357924680")
                .address("Calle Nueva 789")
                .phone("0987123456")
                .build();

        Person savedPerson = Person.builder()
                .personId("2")
                .name("Carlos Rodriguez")
                .gender("Masculino")
                .age((short) 35)
                .identification("1357924680")
                .address("Calle Nueva 789")
                .phone("0987123456")
                .build();

        when(personOutPort.findByIdentification(anyString(), any())).thenReturn(null);
        when(personOutPort.save(any(Person.class))).thenReturn(savedPerson);

        Person result = personUseCase.create(personToCreate);

        assertThat(result).isNotNull();
        assertThat(result.getPersonId()).isEqualTo("2");
        assertThat(result.getName()).isEqualTo("Carlos Rodriguez");
        assertThat(result.getIdentification()).isEqualTo("1357924680");

        verify(personOutPort).findByIdentification(personToCreate.getIdentification(), null);
        verify(personOutPort).save(personToCreate);
    }

    @Test
    void testCreatePerson_DuplicateIdentification_ThrowsValidationException() {
        Person personToCreate = Person.builder()
                .name("Duplicate User")
                .identification("0987654321") // Same as existing person
                .build();

        when(personOutPort.findByIdentification(anyString(), any())).thenReturn(samplePerson);

        assertThatThrownBy(() -> personUseCase.create(personToCreate))
                .isInstanceOf(ValidationException.class)
                .hasMessage("The identification is already in use.");

        verify(personOutPort, never()).save(any(Person.class));
    }

    @Test
    void testFindAllPersons_Success() {
        List<Person> personList = List.of(
                samplePerson,
                Person.builder()
                        .personId("2")
                        .name("Luis Garcia")
                        .identification("1122334455")
                        .build()
        );

        when(personOutPort.findAll()).thenReturn(personList);

        Collection<Person> result = personUseCase.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).contains(samplePerson);

        verify(personOutPort).findAll();
    }

    @Test
    void testFindPersonById_Success() {
        String personId = "1";
        when(personOutPort.findById(personId)).thenReturn(samplePerson);

        Person result = personUseCase.findById(personId);

        assertThat(result).isNotNull();
        assertThat(result.getPersonId()).isEqualTo(personId);
        assertThat(result.getName()).isEqualTo("Ana Martinez");
        assertThat(result.getIdentification()).isEqualTo("0987654321");

        verify(personOutPort).findById(personId);
    }

    @Test
    void testUpdatePerson_Success() {
        String personId = "1";
        Person updateData = Person.builder()
                .personId(personId)
                .name("Ana Updated")
                .phone("0999111222")
                .address("New Address 123")
                .build();

        Person existingPerson = Person.builder()
                .personId(personId)
                .name("Ana Martinez")
                .identification("0987654321")
                .phone("0998765432")
                .address("Av Republica 456")
                .build();

        Person updatedPerson = Person.builder()
                .personId(personId)
                .name("Ana Updated")
                .identification("0987654321")
                .phone("0999111222")
                .address("New Address 123")
                .build();

        when(personOutPort.findById(personId)).thenReturn(existingPerson);
        when(personOutPort.findByIdentification(anyString(), anyString())).thenReturn(null);
        when(personOutPort.save(any(Person.class))).thenReturn(updatedPerson);

        Person result = personUseCase.update(updateData);

        assertThat(result).isNotNull();
        assertThat(result.getPersonId()).isEqualTo(personId);
        assertThat(result.getName()).isEqualTo("Ana Updated");
        assertThat(result.getPhone()).isEqualTo("0999111222");
        assertThat(result.getAddress()).isEqualTo("New Address 123");

        verify(personOutPort).findById(personId);
        verify(personOutPort).findByIdentification(existingPerson.getIdentification(), existingPerson.getIdentification());
        verify(personOutPort).save(any(Person.class));
    }

    @Test
    void testDeletePerson_Success() {
        String personId = "1";

        personUseCase.delete(personId);

        verify(personOutPort).delete(personId);
    }

    @Test
    void testFindByIdentification_Success() {
        String identification = "0987654321";
        String identificationUpdated = null;
        when(personOutPort.findByIdentification(identification, identificationUpdated)).thenReturn(samplePerson);

        Person result = personUseCase.findByIdentification(identification, identificationUpdated);

        assertThat(result).isNotNull();
        assertThat(result.getIdentification()).isEqualTo(identification);
        assertThat(result.getName()).isEqualTo("Ana Martinez");

        verify(personOutPort).findByIdentification(identification, identificationUpdated);
    }

    @Test
    void testFindByIdentification_NotFound() {
        String identification = "9999999999";
        String identificationUpdated = null;
        when(personOutPort.findByIdentification(identification, identificationUpdated)).thenReturn(null);

        Person result = personUseCase.findByIdentification(identification, identificationUpdated);

        assertThat(result).isNull();

        verify(personOutPort).findByIdentification(identification, identificationUpdated);
    }

    @Test
    void testValidateUniqueIdentification_Success() {
        Person personToCreate = Person.builder()
                .name("Unique User")
                .identification("5555555555")
                .build();

        when(personOutPort.findByIdentification(anyString(), any())).thenReturn(null);
        when(personOutPort.save(any(Person.class))).thenReturn(personToCreate);

        Person result = personUseCase.create(personToCreate);

        assertThat(result).isNotNull();
        verify(personOutPort).findByIdentification(personToCreate.getIdentification(), null);
        verify(personOutPort).save(personToCreate);
    }
}
