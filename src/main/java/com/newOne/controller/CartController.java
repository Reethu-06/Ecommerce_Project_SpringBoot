//package com.newOne.controller;
//
//import com.newOne.request.CartRequest;
//import com.newOne.response.CartResponse;
//import com.newOne.service.CartService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/cart")
//public class CartController {
//
//    @Autowired
//    private CartService cartService;
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addToCart(@RequestBody CartRequest request) {
//        return ResponseEntity.ok(cartService.addToCart(request));
//    }
//
//    @PutMapping("/update/{cartId}")
//    public ResponseEntity<String> updateCart(@PathVariable Integer cartId, @RequestParam Integer quantity) {
//        return ResponseEntity.ok(cartService.updateCart(cartId, quantity));
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<CartResponse>> getUserCart(@PathVariable Integer userId) {
//        return ResponseEntity.ok(cartService.getCartByUser(userId));
//    }
//
//    @DeleteMapping("/delete/{cartId}")
//    public ResponseEntity<String> deleteCartItem(@PathVariable Integer cartId) {
//        return ResponseEntity.ok(cartService.deleteCartItem(cartId));
//    }
//}

package com.newOne.controller;

import com.newOne.request.CartRequest;
import com.newOne.response.CartResponse;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing cart functionalities such as:
 * - Adding items to the cart
 * - Updating cart items
 * - Retrieving user's cart
 * - Deleting cart items
 */
@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RoleAccessUtil roleAccessUtil;

    /**
     * Adds a product to the user's cart.
     *
     * @param request     The cart request containing product details.
     * @param httpRequest The HTTP request containing the user token.
     * @return Response with success message or access denial.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request, HttpServletRequest httpRequest) {
        log.info("Received request to add item to cart for user: {}", httpRequest.getAttribute("userId"));

        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for adding item to cart.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Integer userIdFromToken = Integer.valueOf(httpRequest.getAttribute("userId").toString());
        request.setUserId(Long.valueOf(userIdFromToken));

        String response = cartService.addToCart(request);
        log.info("Cart item added successfully for user: {}", userIdFromToken);

        return ResponseEntity.ok(response);
    }

    /**
     * Updates the quantity of a product in the user's cart.
     *
     * @param cartId      The ID of the cart item to update.
     * @param quantity    The new quantity to set.
     * @param httpRequest The HTTP request containing the user token.
     * @return Response with success message or access denial.
     */
    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCart(@PathVariable Integer cartId,
                                             @RequestParam Integer quantity,
                                             HttpServletRequest httpRequest) {
        log.info("Received request to update cart item: {} with quantity: {}", cartId, quantity);

        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for updating cart item.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = cartService.updateCart(cartId, quantity);
        log.info("Cart item updated successfully. Cart ID: {}", cartId);

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the cart items for the logged-in user.
     *
     * @param httpRequest The HTTP request containing the user token.
     * @return List of cart items or null in case of access denial.
     */
    @GetMapping("/user")
    public ResponseEntity<List<CartResponse>> getUserCart(HttpServletRequest httpRequest) {
        log.info("Fetching cart details for user: {}", httpRequest.getAttribute("userId"));

        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for fetching cart details.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Integer userIdFromToken = Integer.valueOf(httpRequest.getAttribute("userId").toString());
        List<CartResponse> cartResponse = cartService.getCartByUser(userIdFromToken);

        log.info("Successfully fetched cart details for user: {}", userIdFromToken);
        return ResponseEntity.ok(cartResponse);
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param cartId      The ID of the cart item to delete.
     * @param httpRequest The HTTP request containing the user token.
     * @return Response with success message or access denial.
     */
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer cartId, HttpServletRequest httpRequest) {
        log.info("Received request to delete cart item: {}", cartId);

        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for deleting cart item.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = cartService.deleteCartItem(cartId);
        log.info("Cart item deleted successfully. Cart ID: {}", cartId);

        return ResponseEntity.ok(response);
    }
}
