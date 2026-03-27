package com.pichincha.customer.domain.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private String title;
    private String detail;
    private List<ErrorDetail> errors;
    private String instance;
    private String type;
    private String resource;
    private String component;
    private String backend;
}
