package com.sample.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Integer id;
    @JsonInclude(NON_NULL)
    private Integer userId;
    private String street;
    private String state;
    private String zip;
}
