package com.sample.dto.common.validator.mapper;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public class ConstraintViolationExceptionMapper extends BaseConstraintExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
}
