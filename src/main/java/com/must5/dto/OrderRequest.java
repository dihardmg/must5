package com.must5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @JsonProperty("customerName")
    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("orderDate")
    private LocalDate orderDate;

    @NotNull(message = "Items are required")
    @Valid
    @JsonProperty("items")
    private List<OrderItemRequest> items;

    public OrderRequest() {
    }

    public OrderRequest(String customerName, List<OrderItemRequest> items) {
        this.customerName = customerName;
        this.items = items;
        this.orderDate = LocalDate.now();
    }

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "customerName='" + customerName + '\'' +
                ", orderDate=" + orderDate +
                ", items=" + items +
                '}';
    }
}