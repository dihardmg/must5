package com.must5.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.smallrye.mutiny.Uni;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries({
    @NamedQuery(
        name = "ReactiveOrder.findByCustomerName",
        query = "SELECT o FROM ReactiveOrder o WHERE o.customerName = :customerName ORDER BY o.orderDate DESC"
    ),
    @NamedQuery(
        name = "ReactiveOrder.findByDateRange",
        query = "SELECT o FROM ReactiveOrder o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC"
    )
})
public class ReactiveOrder extends PanacheEntity {

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
    private List<ReactiveOrderItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ReactiveOrder() {
        this.orderDate = LocalDate.now();
    }

    public ReactiveOrder(String customerName, BigDecimal totalAmount) {
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
                    .map(ReactiveOrderItem::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public void addItem(ReactiveOrderItem item) {
        items.add(item);
        item.setOrder(this);
        updateTotalAmount();
    }

    public void removeItem(ReactiveOrderItem item) {
        items.remove(item);
        item.setOrder(null);
        updateTotalAmount();
    }

    public void calculateTotalAmount() {
        updateTotalAmount();
    }

    // Reactive query methods returning Uni
    public static Uni<List<ReactiveOrder>> findByCustomerName(String customerName) {
        return find("#ReactiveOrder.findByCustomerName",
                  Parameters.with("customerName", customerName)).list();
    }

    public static Uni<List<ReactiveOrder>> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return find("#ReactiveOrder.findByDateRange",
                  Parameters.with("startDate", startDate)
                             .and("endDate", endDate)).list();
    }

    // Reactive method untuk menampilkan total belanja per customer
    public static Uni<List<CustomerSpending>> getTotalSpendingPerCustomer() {
        return findAll()
            .list()
            .map(orders -> {
                java.util.Map<String, java.math.BigDecimal> customerTotals = new java.util.HashMap<>();
                for (io.quarkus.hibernate.reactive.panache.PanacheEntityBase orderBase : orders) {
                    ReactiveOrder order = (ReactiveOrder) orderBase;
                    customerTotals.merge(
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        java.math.BigDecimal::add
                    );
                }
                return customerTotals.entrySet().stream()
                    .map(entry -> new CustomerSpending(entry.getKey(), entry.getValue()))
                    .sorted((a, b) -> b.getTotalSpending().compareTo(a.getTotalSpending()))
                    .collect(java.util.stream.Collectors.toList());
            });
    }

    // Reactive CRUD operations
    public static Uni<ReactiveOrder> findByIdOptionalReactive(Long id) {
        return findById(id);
    }

    public static Uni<List<ReactiveOrder>> findAllReactive() {
        return findAll().list();
    }

    public static Uni<Long> countReactive() {
        return count();
    }

    public static Uni<List<ReactiveOrder>> findWithPagination(int pageIndex, int pageSize, String sortBy, String sortOrder) {
        io.quarkus.panache.common.Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ?
            io.quarkus.panache.common.Sort.Direction.Descending : io.quarkus.panache.common.Sort.Direction.Ascending;
        io.quarkus.panache.common.Sort sort = io.quarkus.panache.common.Sort.by(sortBy, direction);
        return findAll(sort).page(pageIndex, pageSize).list();
    }

    public static Uni<List<ReactiveOrder>> findByCustomerNameWithPagination(String customerName, int pageIndex, int pageSize) {
        return find("customerName = :customerName", Parameters.with("customerName", customerName))
                .page(pageIndex, pageSize).list();
    }

    public static Uni<Long> countByCustomerName(String customerName) {
        return count("customerName", customerName);
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

    public List<ReactiveOrderItem> getItems() {
        return items;
    }

    public void setItems(List<ReactiveOrderItem> items) {
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
        return "ReactiveOrder{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                ", createdAt=" + createdAt +
                '}';
    }
}