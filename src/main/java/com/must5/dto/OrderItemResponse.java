package com.must5.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, String productName, Integer quantity, BigDecimal price, BigDecimal subTotal) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonProperty("subTotal")
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "OrderItemResponse{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subTotal=" + subTotal +
                '}';
    }
}