package com.newOne.service;

import com.newOne.request.CartRequest;  // Used to represent a request to add/update a cart item
import com.newOne.response.CartResponse; // Used to represent the response when fetching cart items
import java.util.List; // Required for returning a list of CartResponse objects

public interface CartService {

    /**
     * Adds an item to the user's cart.
     *
     * @param request Contains the details of the product to be added to the cart (product_id, quantity, etc.).
     * @return A success message indicating the item has been added to the cart.
     */
    String addToCart(CartRequest request);

    /**
     * Updates the quantity of a cart item.
     *
     * @param cartId The ID of the cart item to be updated.
     * @param quantity The new quantity of the cart item.
     * @return A success message indicating the cart item has been updated.
     */
    String updateCart(Integer cartId, Integer quantity);

    /**
     * Retrieves the list of cart items for a specific user.
     *
     * @param userId The ID of the user whose cart items are to be fetched.
     * @return A list of CartResponse objects representing the cart items.
     */
    List<CartResponse> getCartByUser(Integer userId);

    /**
     * Deletes a specific item from the user's cart.
     *
     * @param cartId The ID of the cart item to be deleted.
     * @return A success message indicating the cart item has been deleted.
     */
    String deleteCartItem(Integer cartId);
}
