package com.must5.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.HashMap;

/**
 * Validation Error Response wrapper for structured validation error format
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {

    private int code;
    private String status;
    private String message;
    private Map<String, java.util.List<String>> errors;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(int code, String status, String message, Map<String, java.util.List<String>> errors) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    // Static factory methods
    public static ValidationErrorResponse validationFailed(String field, String errorMessage) {
        Map<String, java.util.List<String>> errors = new HashMap<>();
        errors.put(field, java.util.List.of(errorMessage));
        return new ValidationErrorResponse(400, "BAD_REQUEST", "Validation failed", errors);
    }

    public static ValidationErrorResponse validationFailed(Map<String, java.util.List<String>> errors) {
        return new ValidationErrorResponse(400, "BAD_REQUEST", "Validation failed", errors);
    }

    public static ValidationErrorResponse fromConstraintViolations(java.util.Set<jakarta.validation.ConstraintViolation<?>> violations) {
        Map<String, java.util.List<String>> errors = new HashMap<>();

        for (jakarta.validation.ConstraintViolation<?> violation : violations) {
            String fullPath = violation.getPropertyPath().toString();
            String simpleFieldName = extractSimpleFieldName(fullPath);
            String message = violation.getMessage();

            errors.computeIfAbsent(simpleFieldName, k -> new java.util.ArrayList<>()).add(message);
        }

        return new ValidationErrorResponse(400, "BAD_REQUEST", "Validation failed", errors);
    }

    /**
     * Extract simple field name from the full property path
     * Examples:
     * "createOrder.orderRequest.items[0].quantity" -> "quantity"
     * "items[0].price" -> "price"
     * "customerName" -> "customerName"
     */
    private static String extractSimpleFieldName(String fullPath) {
        if (fullPath == null || fullPath.isEmpty()) {
            return "unknown";
        }

        // Split by dots and get the last part
        String[] parts = fullPath.split("\\.");
        String lastPart = parts[parts.length - 1];

        // If it contains array notation, extract the field name before [index]
        if (lastPart.contains("[")) {
            int bracketIndex = lastPart.indexOf("[");
            return lastPart.substring(0, bracketIndex);
        }

        return lastPart;
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, java.util.List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, java.util.List<String>> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ValidationErrorResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}