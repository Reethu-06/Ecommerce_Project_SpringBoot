package com.newOne.controller;

import com.newOne.request.OrderRequestDto;
import com.newOne.response.OrderResponse;
import com.newOne.response.OrderResponseDto;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing order-related operations such as:
 * - Checkout
 * - Fetching orders
 * - Cancelling orders
 * - Updating order status
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoleAccessUtil roleAccessUtil;

    /**
     * Checkout order for a user.
     *
     * @param orderRequestDto Order details
     * @param httpRequest     HTTP request for user identification
     * @return Order response or access denied
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutOrder(@RequestBody OrderRequestDto orderRequestDto,
                                           HttpServletRequest httpRequest) {
        log.info("Initiating checkout for user order...");

        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied during checkout attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Integer userIdFromToken = Integer.valueOf(httpRequest.getAttribute("userId").toString());
        orderRequestDto.setUserId(userIdFromToken);

        log.info("User ID {} identified. Proceeding with checkout.", userIdFromToken);
        OrderResponseDto response = orderService.checkoutOrder(orderRequestDto);

        log.info("Checkout completed successfully for user ID {}.", userIdFromToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Get orders for a specific user or order ID.
     *
     * @param userId    Optional user ID (Admins only)
     * @param orderId   Optional order ID
     * @param httpRequest HTTP request for access control
     * @return List of orders or access denied
     */
    @GetMapping("/get")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            HttpServletRequest httpRequest) {

        log.info("Fetching orders. UserId: {}, OrderId: {}", userId, orderId);

        if (!roleAccessUtil.hasAdminAccess(httpRequest) && !roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied while attempting to fetch orders.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (roleAccessUtil.hasUserAccess(httpRequest)) {
            Integer userIdFromToken = Integer.valueOf(httpRequest.getAttribute("userId").toString());
            userId = (userId == null) ? userIdFromToken.longValue() : userId;

            log.info("User access confirmed. Fetching orders for User ID: {}", userId);
        }

        List<OrderResponse> orders = orderService.getOrders(userId, orderId);
        log.info("Fetched {} orders successfully.", orders.size());
        return ResponseEntity.ok(orders);
    }

    /**
     * Cancel an order (User or Admin access).
     *
     * @param orderId   Order ID to cancel
     * @param httpRequest HTTP request for access control
     * @return Success message or access denied
     */
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId, HttpServletRequest httpRequest) {
        log.info("Attempting to cancel order with ID: {}", orderId);

        if (!roleAccessUtil.hasAdminAccess(httpRequest) && !roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied while attempting to cancel order.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        orderService.cancelOrder(orderId);
        log.info("Order with ID {} cancelled successfully.", orderId);

        return ResponseEntity.ok("Order cancelled successfully.");
    }

    /**
     * Update order status (Admin only).
     *
     * @param orderId   Order ID to update
     * @param newStatus New status ID
     * @param httpRequest HTTP request for access control
     * @return Success message or access denied
     */
    @PutMapping("/update/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int orderId,
                                                    @RequestParam String newStatus,
                                                    HttpServletRequest httpRequest) {
        log.info("Updating order status. Order ID: {}, New Status: {}", orderId, newStatus);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied during order status update attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        orderService.updateOrder(orderId, Integer.parseInt(newStatus));
        log.info("Order status updated successfully for Order ID: {}", orderId);

        return ResponseEntity.ok("Order status updated successfully.");
    }
}
