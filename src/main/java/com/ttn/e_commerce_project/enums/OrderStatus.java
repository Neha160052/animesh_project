package com.ttn.e_commerce_project.enums;

import java.util.Map;
import java.util.Set;

public enum OrderStatus {

    ORDER_PLACED,
    CANCELLED,
    ORDER_REJECTED,
    ORDER_CONFIRMED,
    ORDER_SHIPPED,
    DELIVERED,
    RETURN_REQUESTED,
    RETURN_REJECTED,
    RETURN_APPROVED,
    PICK_UP_INITIATED,
    PICK_UP_COMPLETED,
    REFUND_INITIATED,
    REFUND_COMPLETED,
    CLOSED;

    private static final Map<OrderStatus, Set<OrderStatus>> transitions= Map.ofEntries(
            Map.entry(ORDER_PLACED, Set.of(CANCELLED, ORDER_CONFIRMED, ORDER_REJECTED)),
            Map.entry(CANCELLED, Set.of(REFUND_INITIATED, CLOSED)),
            Map.entry(ORDER_REJECTED, Set.of(REFUND_INITIATED, CLOSED)),
            Map.entry(ORDER_CONFIRMED, Set.of(CANCELLED, ORDER_SHIPPED)),
            Map.entry(ORDER_SHIPPED, Set.of(DELIVERED)),
            Map.entry(DELIVERED, Set.of(RETURN_REQUESTED, CLOSED)),
            Map.entry(RETURN_REQUESTED, Set.of(RETURN_REJECTED, RETURN_APPROVED)),
            Map.entry(RETURN_REJECTED, Set.of(CLOSED)),
            Map.entry(RETURN_APPROVED, Set.of(PICK_UP_INITIATED)),
            Map.entry(PICK_UP_INITIATED, Set.of(PICK_UP_COMPLETED)),
            Map.entry(PICK_UP_COMPLETED, Set.of(REFUND_INITIATED)),
            Map.entry(REFUND_INITIATED, Set.of(REFUND_COMPLETED)),
            Map.entry(REFUND_COMPLETED, Set.of(CLOSED)),
            Map.entry(CLOSED, Set.of())
    );

}
