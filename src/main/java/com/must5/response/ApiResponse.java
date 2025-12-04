package com.must5.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic API Response wrapper for consistent response format
 * @param <T> the type of the data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code;
    private String status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "SUCCESS", "Operation completed successfully", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "CREATED", "Resource created successfully", data);
    }

    public static <T> ApiResponse<T> accepted(T data) {
        return new ApiResponse<>(202, "ACCEPTED", "Request accepted for processing", data);
    }

    public static <T> ApiResponse<T> notFound() {
        return new ApiResponse<>(404, "NOT_FOUND", "Resource not found", null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, "BAD_REQUEST", message, null);
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return new ApiResponse<>(500, "INTERNAL_SERVER_ERROR", message, null);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}