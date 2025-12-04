package com.must5.exception;

import com.must5.response.ValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.*;

/**
 * Exception mapper for handling validation errors
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ValidationErrorResponse errorResponse = ValidationErrorResponse.fromConstraintViolations(exception.getConstraintViolations());
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}