package com.must5.resource;

import com.must5.dto.OrderItemRequest;
import com.must5.dto.OrderItemResponse;
import com.must5.dto.OrderRequest;
import com.must5.dto.OrderResponse;
import com.must5.entity.CustomerSpending;
import com.must5.entity.ReactiveOrder;
import com.must5.entity.ReactiveOrderItem;
import com.must5.response.ApiResponse;
import com.must5.response.PaginatedApiResponse;
import com.must5.response.ValidationErrorResponse;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/reactive/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reactive Order Management", description = "API untuk mengelola order (reactive operations)")
@ApplicationScoped
@PermitAll
public class ReactiveOrderResource {

    @POST
    @Operation(
        summary = "Create new order (reactive)",
        description = "Create a new order with items using reactive operations. Returns the created order with ID."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "201",
                description = "Order created successfully (reactive)"
            ),
            @APIResponse(
                responseCode = "400",
                description = "Bad request - validation failed"
            )
        }
    )
    @RequestBody(
        description = "Order to create (reactive)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "ReactiveOrderExample",
                value = "{\"customerName\":\"John Doe\",\"orderDate\":\"2025-12-04\",\"items\":[{\"productName\":\"Test Product\",\"quantity\":2,\"price\":75.00}]}"
            )
        )
    )
    public Uni<Response> createOrder(@Valid OrderRequest orderRequest, @Context UriInfo uriInfo) {
        // Manual validation for empty items
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            ValidationErrorResponse errorResponse = ValidationErrorResponse.validationFailed(
                "items", "Order must have at least one item"
            );
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build());
        }

        // Validate individual items
        java.util.Map<String, java.util.List<String>> validationErrors = new java.util.HashMap<>();

        for (int i = 0; i < orderRequest.getItems().size(); i++) {
            OrderItemRequest itemRequest = orderRequest.getItems().get(i);
            String fieldPrefix = "items[" + i + "].";

            // Validate quantity
            if (itemRequest.getQuantity() == null ||
                itemRequest.getQuantity().toString().trim().isEmpty() ||
                itemRequest.getQuantity() <= 0) {
                validationErrors.computeIfAbsent("quantity", k -> new java.util.ArrayList<>())
                    .add("Quantity is required and must be greater than 0");
            }

            // Validate price
            if (itemRequest.getPrice() == null ||
                itemRequest.getPrice().toString().trim().isEmpty() ||
                itemRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                validationErrors.computeIfAbsent("price", k -> new java.util.ArrayList<>())
                    .add("Price is required and must be greater than 0");
            }

            // Validate productName
            if (itemRequest.getProductName() == null || itemRequest.getProductName().trim().isEmpty()) {
                validationErrors.computeIfAbsent("productName", k -> new java.util.ArrayList<>())
                    .add("Product name is required");
            }
        }

        // If there are validation errors, return them
        if (!validationErrors.isEmpty()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                400,
                "BAD_REQUEST",
                "Validation failed",
                validationErrors
            );
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build());
        }

        ReactiveOrder order = new ReactiveOrder();
        order.setCustomerName(orderRequest.getCustomerName());
        if (orderRequest.getOrderDate() != null) {
            order.setOrderDate(orderRequest.getOrderDate());
        }

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            ReactiveOrderItem item = new ReactiveOrderItem();
            item.setProductName(itemRequest.getProductName());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            order.addItem(item);
        }

        return order.<ReactiveOrder>persist()
                .onItem().transform(persistedOrder -> {
                    OrderResponse orderData = convertToOrderResponse(persistedOrder);
                    ApiResponse<OrderResponse> response = new ApiResponse<>(
                        201,
                        "CREATED",
                        "order created successfully",
                        orderData
                    );
                    URI location = uriInfo.getAbsolutePathBuilder().path(persistedOrder.id.toString()).build();
                    return Response.created(location).entity(response).build();
                })
                .onFailure().recoverWithItem(throwable -> {
                    ApiResponse<Void> errorResponse = new ApiResponse<>(
                        400,
                        "BAD_REQUEST",
                        "Failed to create order: " + throwable.getMessage(),
                        null
                    );
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
                });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getOrderById(@PathParam("id") Long id) {
        return ReactiveOrder.findByIdOptionalReactive(id)
                .onItem().transform(orderOpt -> {
                    if (orderOpt == null) {
                        ApiResponse<Void> response = new ApiResponse<>(
                            404,
                            "NOT_FOUND",
                            "Order not found",
                            null
                        );
                        return Response.status(Response.Status.NOT_FOUND).entity(response).build();
                    }

                    OrderResponse orderData = convertToOrderResponse(orderOpt);
                    ApiResponse<OrderResponse> response = new ApiResponse<>(
                        200,
                        "SUCCESS",
                        "Order retrieved successfully",
                        orderData
                    );
                    return Response.ok(response).build();
                });
    }

    @GET
    public Uni<Response> getOrders() {
        // Simple reactive GET that returns a message
        return Uni.createFrom().item(() -> {
            ApiResponse<String> response = new ApiResponse<>(
                200,
                "SUCCESS",
                "Reactive orders endpoint is working. For data operations, please use POST to create orders or use /orders for full CRUD operations.",
                "Reactive GET is limited due to session management complexities in current setup."
            );
            return Response.ok(response).build();
        });
    }

    @GET
    @Path("/customers/spending")
    public Uni<Response> getTotalSpendingPerCustomer(
            @QueryParam("page") @DefaultValue("1") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize) {

        // Convert 1-based page index to 0-based for internal use
        int page = Math.max(0, pageIndex - 1);
        int size = Math.min(100, Math.max(1, pageSize));

        return ReactiveOrder.getTotalSpendingPerCustomer()
                .onItem().transform(allCustomerSpendings -> {
                    long totalElements = allCustomerSpendings.size();

                    // Apply pagination manually
                    int startIndex = page * size;
                    int endIndex = Math.min(startIndex + size, (int) totalElements);

                    List<CustomerSpending> customerSpendings;
                    if (startIndex >= totalElements) {
                        // If start index is beyond the list, return empty list
                        customerSpendings = List.of();
                    } else {
                        customerSpendings = allCustomerSpendings.subList(startIndex, endIndex);
                    }

                    PaginatedApiResponse.PaginationInfo paginationInfo =
                        new PaginatedApiResponse.PaginationInfo(totalElements, pageIndex, size, (int) Math.ceil((double) totalElements / size));

                    PaginatedApiResponse<CustomerSpending> response = new PaginatedApiResponse<>(
                        200,
                        "SUCCESS",
                        "Customer spending data retrieved successfully",
                        customerSpendings,
                        paginationInfo
                    );

                    return Response.ok(response).build();
                })
                .onFailure().recoverWithItem(throwable -> {
                    ApiResponse<Void> errorResponse = new ApiResponse<>(
                        500,
                        "INTERNAL_SERVER_ERROR",
                        "Failed to retrieve customer spending data: " + throwable.getMessage(),
                        null
                    );
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
                });
    }

    @GET
    @Path("/customers/{customerName}")
    public Uni<Response> getOrdersByCustomer(
            @PathParam("customerName") String customerName,
            @QueryParam("page") @DefaultValue("1") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize) {

        // Convert 1-based page index to 0-based for internal use
        int page = Math.max(0, pageIndex - 1);
        int size = Math.min(100, Math.max(1, pageSize));

        return Uni.combine().all().unis(
                ReactiveOrder.findByCustomerNameWithPagination(customerName, page, size),
                ReactiveOrder.countByCustomerName(customerName)
            ).combinedWith((orders, totalElements) -> {
                List<OrderResponse> orderResponses = orders.stream()
                        .map(this::convertToOrderResponse)
                        .collect(Collectors.toList());

                PaginatedApiResponse.PaginationInfo paginationInfo =
                    new PaginatedApiResponse.PaginationInfo(totalElements, pageIndex, size, (int) Math.ceil((double) totalElements / size));

                PaginatedApiResponse<OrderResponse> response = new PaginatedApiResponse<>(
                    200,
                    "SUCCESS",
                    "Orders for customer '" + customerName + "' retrieved successfully",
                    orderResponses,
                    paginationInfo
                );

                return Response.ok(response).build();
            })
            .onFailure().recoverWithItem(throwable -> {
                ApiResponse<Void> errorResponse = new ApiResponse<>(
                    400,
                    "BAD_REQUEST",
                    "Failed to retrieve orders for customer: " + throwable.getMessage(),
                    null
                );
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            });
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteOrder(@PathParam("id") Long id) {
        return ReactiveOrder.findByIdOptionalReactive(id)
                .onItem().transformToUni(orderOpt -> {
                    if (orderOpt == null) {
                        ApiResponse<Void> response = new ApiResponse<>(
                            404,
                            "NOT_FOUND",
                            "Order not found",
                            null
                        );
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).entity(response).build());
                    }

                    return orderOpt.delete()
                            .onItem().transform(deleted -> {
                                ApiResponse<Void> response = new ApiResponse<>(
                                    204,
                                    "NO_CONTENT",
                                    "Order deleted successfully",
                                    null
                                );
                                return Response.noContent().entity(response).build();
                            });
                })
                .onFailure().recoverWithItem(throwable -> {
                    ApiResponse<Void> errorResponse = new ApiResponse<>(
                        400,
                        "BAD_REQUEST",
                        "Failed to delete order: " + throwable.getMessage(),
                        null
                    );
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
                });
    }

    private OrderResponse convertToOrderResponse(ReactiveOrder order) {
        List<OrderItemResponse> itemResponses = null;
        if (order.getItems() != null) {
            itemResponses = order.getItems().stream()
                    .map(item -> new OrderItemResponse(
                            item.id,
                            item.getProductName(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getSubTotal()
                    ))
                    .collect(Collectors.toList());
        }

        return new OrderResponse(
                order.id,
                order.getCustomerName(),
                order.getOrderDate(),
                order.getTotalAmount(),
                itemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    // Helper class for pagination response
    public static class PaginationResponse {
        private List<OrderResponse> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;

        public PaginationResponse(List<OrderResponse> content, int page, int size, long totalElements) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.first = page == 0;
            this.last = page >= totalPages - 1;
        }

        // Getters
        public List<OrderResponse> getContent() { return content; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean isFirst() { return first; }
        public boolean isLast() { return last; }
    }
}