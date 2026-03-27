package com.pichincha.customer.infrastructure.exception;

import java.util.ArrayList;
import java.util.List;

import com.pichincha.customer.application.exception.DatabaseException;
import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.application.exception.SystemException;
import com.pichincha.customer.application.exception.ValidationException;
import com.pichincha.customer.domain.error.CustomErrorResponse;
import com.pichincha.customer.domain.error.ErrorDetail;
import com.pichincha.customer.domain.util.Constants;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String COMPONENT = "customer-service";
    private static final String BACKEND = "customer-backend";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandling(Exception exception, WebRequest request){
        log.error("Unhandled exception occurred", exception);
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_INTERNAL_ERROR)
                .detail(Constants.INTERNAL_ERROR)
                .errors(List.of(createErrorDetail(Constants.CODIGO_ERROR_05, Constants.CATALOG_INTERNAL_ERROR, Constants.INTERNAL_ERROR)))
                .instance(Constants.CODIGO_ERROR_05)
                .type(extractPath(request))
                .resource(exception.getClass().getSimpleName())
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request){
        ErrorDetail errorDetail = mapResourceNotFoundException(exception.getMessage());
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(errorDetail.getMessage())
                .detail(exception.getMessage())
                .errors(List.of(errorDetail))
                .instance(errorDetail.getCode())
                .type(extractPath(request))
                .resource(Constants.NOT_FOUND_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationHandling(ValidationException exception, WebRequest request){
        ErrorDetail errorDetail = mapValidationException(exception.getMessage());
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(errorDetail.getMessage())
                .detail(exception.getMessage())
                .errors(List.of(errorDetail))
                .instance(errorDetail.getCode())
                .type(extractPath(request))
                .resource(Constants.VALIDATION_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> jsonNotReadableHandling(HttpMessageNotReadableException exception, WebRequest request){
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_VALIDATION_ERROR)
                .detail(Constants.VALIDATION_ERROR_DETAIL)
                .errors(List.of(createErrorDetail(Constants.CODIGO_ERROR_02, Constants.CATALOG_VALIDATION_ERROR, Constants.VALIDATION_ERROR)))
                .instance(Constants.CODIGO_ERROR_02)
                .type(extractPath(request))
                .resource(Constants.READABLE_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> badRequestValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        List<ErrorDetail> errorDetails = mapFieldErrors(exception.getBindingResult().getFieldErrors());
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_EXCEPTION_BAD_REQUEST)
                .detail(Constants.GENERIC_ERROR)
                .errors(errorDetails)
                .instance(Constants.INSTANCE)
                .type(extractPath(request))
                .resource(Constants.ARGUMENT_NOT_VALID_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> systemExceptionHandling(SystemException exception, WebRequest request){
        log.error("System error occurred", exception);
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_INTERNAL_ERROR)
                .detail(exception.getMessage())
                .errors(List.of(createErrorDetail(Constants.CODIGO_ERROR_05, Constants.CATALOG_INTERNAL_ERROR, Constants.INTERNAL_ERROR)))
                .instance(Constants.CODIGO_ERROR_05)
                .type(extractPath(request))
                .resource(Constants.SYSTEM_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Object> databaseExceptionHandling(DatabaseException exception, WebRequest request){
        log.error("Database error occurred", exception);
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_DATABASE_ERROR)
                .detail(exception.getMessage())
                .errors(List.of(createErrorDetail(Constants.CODIGO_ERROR_06, Constants.CATALOG_DATABASE_ERROR, Constants.DATABASE_ERROR)))
                .instance(Constants.CODIGO_ERROR_06)
                .type(extractPath(request))
                .resource(Constants.DATABASE_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> dataAccessExceptionHandling(DataAccessException exception, WebRequest request){
        log.error("Data access error", exception);
        
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .title(Constants.CATALOG_DATABASE_ERROR)
                .detail(Constants.DATABASE_ERROR)
                .errors(List.of(createErrorDetail(Constants.CODIGO_ERROR_06, Constants.CATALOG_DATABASE_ERROR, Constants.DATABASE_ERROR)))
                .instance(Constants.CODIGO_ERROR_06)
                .type(extractPath(request))
                .resource(Constants.DATA_ACCESS_EXCEPTION)
                .component(COMPONENT)
                .backend(BACKEND)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ErrorDetail createErrorDetail(String code, String message, String businessMessage) {
        return ErrorDetail.builder()
                .code(code)
                .message(message)
                .businessMessage(businessMessage)
                .build();
    }
    
    private List<ErrorDetail> mapFieldErrors(List<FieldError> fieldErrors) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            ErrorDetail errorDetail = getErrorDetailForField(fieldName, errorMessage);
            
            errorDetails.add(errorDetail);
        }
        
        return errorDetails;
    }
    
    private ErrorDetail getErrorDetailForField(String fieldName, String errorMessage) {
        switch (fieldName.toLowerCase()) {
            case "name":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_100, Constants.CATALOG_NAME_NULL_OR_EMPTY, Constants.NAME_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_101, Constants.CATALOG_NAME_INVALID_FORMAT, Constants.NAME_INVALID_FORMAT);
                
            case "gender":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_110, Constants.CATALOG_GENDER_NULL_OR_EMPTY, Constants.GENDER_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_111, Constants.CATALOG_GENDER_INVALID_FORMAT, Constants.GENDER_INVALID_FORMAT);
                
            case "age":
                return createErrorDetail(Constants.CODIGO_ERROR_120, Constants.CATALOG_AGE_INVALID_RANGE, Constants.AGE_INVALID_RANGE);
                
            case "identification":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_130, Constants.CATALOG_IDENTIFICATION_NULL_OR_EMPTY, Constants.IDENTIFICATION_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_131, Constants.CATALOG_IDENTIFICATION_INVALID_FORMAT, Constants.IDENTIFICATION_INVALID_FORMAT);
                
            case "address":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_140, Constants.CATALOG_ADDRESS_NULL_OR_EMPTY, Constants.ADDRESS_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_141, Constants.CATALOG_ADDRESS_INVALID_FORMAT, Constants.ADDRESS_INVALID_FORMAT);
                
            case "phone":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_150, Constants.CATALOG_PHONE_NULL_OR_EMPTY, Constants.PHONE_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_151, Constants.CATALOG_PHONE_INVALID_FORMAT, Constants.PHONE_INVALID_FORMAT);
                
            case "password":
                if (errorMessage != null && errorMessage.contains("blank")) {
                    return createErrorDetail(Constants.CODIGO_ERROR_200, Constants.CATALOG_PASSWORD_NULL_OR_EMPTY, Constants.PASSWORD_NULL_OR_EMPTY);
                }
                return createErrorDetail(Constants.CODIGO_ERROR_201, Constants.CATALOG_PASSWORD_INVALID_FORMAT, Constants.PASSWORD_INVALID_FORMAT);
                
            default:
                return createErrorDetail(Constants.CODIGO_ERROR_02, Constants.CATALOG_VALIDATION_ERROR, Constants.VALIDATION_ERROR);
        }
    }
    
    private ErrorDetail mapValidationException(String message) {
        if (message.contains("identification") && message.contains("registered")) {
            return createErrorDetail(Constants.CODIGO_ERROR_132, Constants.CATALOG_IDENTIFICATION_DUPLICATE, Constants.IDENTIFICATION_DUPLICATE);
        }
        if (message.contains("Customer") && message.contains("null")) {
            return createErrorDetail(Constants.CODIGO_ERROR_300, Constants.CATALOG_PERSON_NULL, Constants.PERSON_NULL);
        }
        if (message.contains("Person") && message.contains("null")) {
            return createErrorDetail(Constants.CODIGO_ERROR_300, Constants.CATALOG_PERSON_NULL, Constants.PERSON_NULL);
        }
        if (message.contains("ID") && message.contains("null")) {
            return createErrorDetail(Constants.CODIGO_ERROR_210, Constants.CATALOG_CUSTOMER_ID_NULL_OR_EMPTY, Constants.CUSTOMER_ID_NULL_OR_EMPTY);
        }
        if (message.contains("Required field")) {
            return createErrorDetail(Constants.CODIGO_ERROR_02, Constants.CATALOG_VALIDATION_ERROR, Constants.VALIDATION_ERROR);
        }
        return createErrorDetail(Constants.CODIGO_ERROR_01, Constants.CATALOG_EXCEPTION_BAD_REQUEST, Constants.GENERIC_ERROR);
    }
    
    private ErrorDetail mapResourceNotFoundException(String message) {
        if (message.contains("Customer")) {
            return createErrorDetail(Constants.CODIGO_ERROR_211, Constants.CATALOG_CUSTOMER_NOT_FOUND, Constants.CUSTOMER_NOT_FOUND);
        }
        if (message.contains("Person")) {
            return createErrorDetail(Constants.CODIGO_ERROR_311, Constants.CATALOG_PERSON_NOT_FOUND, Constants.PERSON_NOT_FOUND);
        }
        return createErrorDetail(Constants.CODIGO_ERROR_03, Constants.CATALOG_RESOURCE_NOT_FOUND, Constants.RESOURCE_NOT_FOUND);
    }
    
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        if (description.startsWith("uri=")) {
            return description.substring(4);
        }
        return description;
    }
}
