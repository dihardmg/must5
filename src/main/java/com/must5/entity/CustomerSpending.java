package com.must5.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CustomerSpending {

    private String customerName;
    private BigDecimal totalSpending;

    public CustomerSpending() {
    }

    public CustomerSpending(String customerName, BigDecimal totalSpending) {
        this.customerName = customerName;
        this.totalSpending = totalSpending;
    }

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("totalSpending")
    public BigDecimal getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(BigDecimal totalSpending) {
        this.totalSpending = totalSpending;
    }

    @Override
    public String toString() {
        return "CustomerSpending{" +
                "customerName='" + customerName + '\'' +
                ", totalSpending=" + totalSpending +
                '}';
    }
}