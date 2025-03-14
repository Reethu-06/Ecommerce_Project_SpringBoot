
package com.newOne.service;

import com.newOne.request.OrderRequestDto;

import com.newOne.response.OrderGetterResponse;
import com.newOne.response.OrderResponse;
import com.newOne.response.OrderResponseDto;

import java.util.List;


public interface OrderService {

    /**
     * Processes the checkout of an order.
     * This includes checking stock, applying discounts, and updating user wallet balance.
     *
     * @param request Contains the details of the order (items, payment, user info, etc.).
     * @return An OrderResponseDto containing order details and status after checkout.
     */
    OrderResponseDto checkoutOrder(OrderRequestDto request);

    /**
     * Retrieves a list of orders for a given user, with an optional order ID filter.
     *
     * @param userId The ID of the user for whom orders are being fetched.
     * @param orderId The ID of the specific order (optional, can be used for filtering).
     * @return A list of OrderResponse containing details about each order.
     */
    List<OrderGetterResponse> getOrders(Long userId, Long orderId);

    /**
     * Updates the status of a given order.
     * Typically used for admin functionalities such as marking orders as shipped, delivered, etc.
     *
     * @param orderId The ID of the order to be updated.
     * @param StatusId The new status ID to be set for the order (e.g., shipped, delivered).
     * @return An updated OrderResponse with the new status.
     */
    OrderResponse updateOrder(int orderId, int StatusId);

    /**
     * Cancels an existing order and handles associated actions such as refunding the user.
     *
     * @param orderId The ID of the order to be canceled.
     */
    void cancelOrder(int orderId);
}