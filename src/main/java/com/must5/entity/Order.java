package com.must5.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries({
    @NamedQuery(
        name = "Order.findByCustomerName",
        query = "SELECT o FROM Order o WHERE o.customerName = :customerName ORDER BY o.orderDate DESC"
    ),
    @NamedQuery(
        name = "Order.findByDateRange",
        query = "SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC"
    )
})
public class Order extends PanacheEntity {

    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotNull(message = "Order date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public Order() {
        this.orderDate = LocalDate.now();
    }

    public Order(String customerName, BigDecimal totalAmount) {
        this();
        this.customerName = customerName;
        this.totalAmount = totalAmount;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        updateTotalAmount();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        if (items != null) {
            totalAmount = items.stream()
                    .map(OrderItem::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        updateTotalAmount();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        updateTotalAmount();
    }

    public void calculateTotalAmount() {
        updateTotalAmount();
    }

    // Static query methods
    public static List<Order> findByCustomerName(String customerName) {
        return find("#Order.findByCustomerName",
                  Parameters.with("customerName", customerName)).list();
    }

    public static List<Order> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return find("#Order.findByDateRange",
                  Parameters.with("startDate", startDate)
                             .and("endDate", endDate)).list();
    }

    // Query untuk menampilkan total belanja per customer
    public static List<CustomerSpending> getTotalSpendingPerCustomer() {
        return getEntityManager().createQuery(
            "SELECT new com.must5.entity.CustomerSpending(o.customerName, SUM(o.totalAmount)) " +
            "FROM Order o " +
            "GROUP BY o.customerName " +
            "ORDER BY SUM(o.totalAmount) DESC",
            CustomerSpending.class).getResultList();
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        if (items != null) {
            items.forEach(item -> item.setOrder(this));
        }
        updateTotalAmount();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                ", createdAt=" + createdAt +
                '}';
    }
}