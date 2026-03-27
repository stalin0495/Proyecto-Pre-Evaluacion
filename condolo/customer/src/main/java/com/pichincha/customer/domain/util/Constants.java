package com.pichincha.customer.domain.util;

public class Constants {
    Constants(){
        throw  new IllegalStateException("Constant class");
    }

    public static final String CODIGO_ERROR_01 = "001";
    public static final String CATALOG_EXCEPTION_BAD_REQUEST = "Bad Request";
    public static final String GENERIC_ERROR = "Se produjo un error. Verifique los datos";

    public static final String CODIGO_ERROR_02 = "002";
    public static final String CATALOG_VALIDATION_ERROR = "Validation Error";
    public static final String VALIDATION_ERROR = "Error de validación en los datos proporcionados";
    public static final String VALIDATION_ERROR_DETAIL = "El formato del JSON es inválido o no se puede leer";

    public static final String CODIGO_ERROR_03 = "003";
    public static final String CATALOG_RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String RESOURCE_NOT_FOUND = "El recurso solicitado no fue encontrado";

    public static final String CODIGO_ERROR_04 = "004";
    public static final String CATALOG_DUPLICATE_RESOURCE = "Duplicate Resource";
    public static final String DUPLICATE_RESOURCE = "El recurso ya existe en el sistema";

    public static final String CODIGO_ERROR_05 = "005";
    public static final String CATALOG_INTERNAL_ERROR = "Internal Server Error";
    public static final String INTERNAL_ERROR = "Error interno del servidor. Intente mas tarde";

    public static final String CODIGO_ERROR_06 = "006";
    public static final String CATALOG_DATABASE_ERROR = "Database Error";
    public static final String DATABASE_ERROR = "Error en la base de datos";

    public static final String CODIGO_ERROR_100 = "100";
    public static final String CATALOG_NAME_NULL_OR_EMPTY = "Bad Request";
    public static final String NAME_NULL_OR_EMPTY = "El nombre es nulo o vacío";

    public static final String CODIGO_ERROR_101 = "101";
    public static final String CATALOG_NAME_INVALID_FORMAT = "Bad Request";
    public static final String NAME_INVALID_FORMAT = "El nombre contiene caracteres inválidos";

    public static final String CODIGO_ERROR_110 = "110";
    public static final String CATALOG_GENDER_NULL_OR_EMPTY = "Bad Request";
    public static final String GENDER_NULL_OR_EMPTY = "El género es nulo o vacío";

    public static final String CODIGO_ERROR_111 = "111";
    public static final String CATALOG_GENDER_INVALID_FORMAT = "Bad Request";
    public static final String GENDER_INVALID_FORMAT = "El género contiene caracteres inválidos o excede el tamaño permitido";

    public static final String CODIGO_ERROR_120 = "120";
    public static final String CATALOG_AGE_INVALID_RANGE = "Bad Request";
    public static final String AGE_INVALID_RANGE = "La edad debe estar entre 0 y 255";

    public static final String CODIGO_ERROR_130 = "130";
    public static final String CATALOG_IDENTIFICATION_NULL_OR_EMPTY = "Bad Request";
    public static final String IDENTIFICATION_NULL_OR_EMPTY = "La identificación es nula o vacía";

    public static final String CODIGO_ERROR_131 = "131";
    public static final String CATALOG_IDENTIFICATION_INVALID_FORMAT = "Bad Request";
    public static final String IDENTIFICATION_INVALID_FORMAT = "La identificación debe ser un número de 10 dígitos";

    public static final String CODIGO_ERROR_132 = "132";
    public static final String CATALOG_IDENTIFICATION_DUPLICATE = "Bad Request";
    public static final String IDENTIFICATION_DUPLICATE = "La identificación ya está registrada en el sistema";

    public static final String CODIGO_ERROR_140 = "140";
    public static final String CATALOG_ADDRESS_NULL_OR_EMPTY = "Bad Request";
    public static final String ADDRESS_NULL_OR_EMPTY = "La dirección es nula o vacía";

    public static final String CODIGO_ERROR_141 = "141";
    public static final String CATALOG_ADDRESS_INVALID_FORMAT = "Bad Request";
    public static final String ADDRESS_INVALID_FORMAT = "La dirección contiene caracteres inválidos o excede el tamaño permitido";

    public static final String CODIGO_ERROR_150 = "150";
    public static final String CATALOG_PHONE_NULL_OR_EMPTY = "Bad Request";
    public static final String PHONE_NULL_OR_EMPTY = "El teléfono es nulo o vacío";

    public static final String CODIGO_ERROR_151 = "151";
    public static final String CATALOG_PHONE_INVALID_FORMAT = "Bad Request";
    public static final String PHONE_INVALID_FORMAT = "El teléfono debe ser un número de 10 dígitos";

    public static final String CODIGO_ERROR_200 = "200";
    public static final String CATALOG_PASSWORD_NULL_OR_EMPTY = "Bad Request";
    public static final String PASSWORD_NULL_OR_EMPTY = "La contraseña es nula o vacía";

    public static final String CODIGO_ERROR_201 = "201";
    public static final String CATALOG_PASSWORD_INVALID_FORMAT = "Bad Request";
    public static final String PASSWORD_INVALID_FORMAT = "La contraseña debe tener entre 8 y 20 caracteres, incluir mayúscula, minúscula, número y carácter especial";

    public static final String CODIGO_ERROR_210 = "210";
    public static final String CATALOG_CUSTOMER_ID_NULL_OR_EMPTY = "Bad Request";
    public static final String CUSTOMER_ID_NULL_OR_EMPTY = "El ID del cliente es nulo o vacío";

    public static final String CODIGO_ERROR_211 = "211";
    public static final String CATALOG_CUSTOMER_NOT_FOUND = "Bad Request";
    public static final String CUSTOMER_NOT_FOUND = "El cliente no fue encontrado";

    public static final String CODIGO_ERROR_300 = "300";
    public static final String CATALOG_PERSON_NULL = "Bad Request";
    public static final String PERSON_NULL = "Los datos de la persona no pueden ser nulos";

    public static final String CODIGO_ERROR_310 = "310";
    public static final String CATALOG_PERSON_ID_NULL_OR_EMPTY = "Bad Request";
    public static final String PERSON_ID_NULL_OR_EMPTY = "El ID de la persona es nulo o vacío";

    public static final String CODIGO_ERROR_311 = "311";
    public static final String CATALOG_PERSON_NOT_FOUND = "Bad Request";
    public static final String PERSON_NOT_FOUND = "La persona no fue encontrada";

    public static final String NOT_BLANK = "Field cannot be blank";

    public static final String VALIDATION_EXCEPTION = "ValidationException";
    public static final String READABLE_EXCEPTION = "HttpMessageNotReadableException";
    public static final String ARGUMENT_NOT_VALID_EXCEPTION = "MethodArgumentNotValidException";
    public static final String SYSTEM_EXCEPTION = "SystemException";
    public static final String DATABASE_EXCEPTION = "DatabaseException";
    public static final String DATA_ACCESS_EXCEPTION = "DataAccessException";
    public static final String NOT_FOUND_EXCEPTION = "ResourceNotFoundException";
    public static final String INSTANCE = "0188";



}
