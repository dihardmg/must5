package com.must5.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.smallrye.mutiny.Uni;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Table(name = "order_items")
public class ReactiveOrderItem extends PanacheEntity {

    @NotBlank(message = "Product name is required")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private ReactiveOrder order;

    public ReactiveOrderItem() {
    }

    public ReactiveOrderItem(String productName, Integer quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public ReactiveOrderItem(String productName, Integer quantity, BigDecimal price, ReactiveOrder order) {
        this(productName, quantity, price);
        this.order = order;
    }

    // Calculate subtotal (quantity * price)
    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    // Static reactive query methods
    public static Uni<ReactiveOrderItem> findByIdOptionalReactive(Long id) {
        return findById(id);
    }

    public static Uni<List<ReactiveOrderItem>> findByOrderId(Long orderId) {
        return find("order.id", orderId).list();
    }

    public static Uni<Long> countByOrderId(Long orderId) {
        return count("order.id", orderId);
    }

    // Getters and Setters
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

    public ReactiveOrder getOrder() {
        return order;
    }

    public void setOrder(ReactiveOrder order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ReactiveOrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subTotal=" + getSubTotal() +
                ", orderId=" + (order != null ? order.id : null) +
                '}';
    }
}