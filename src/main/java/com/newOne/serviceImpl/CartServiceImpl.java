package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.Cart;
import com.newOne.entity.Product;
import com.newOne.entity.User;
import com.newOne.repository.CartRepository;
import com.newOne.repository.ProductRepository;
import com.newOne.repository.UserRepository;
import com.newOne.request.CartProductRequest;
import com.newOne.request.CartRequest;
import com.newOne.response.CartResponse;
import com.newOne.service.CartService;
import lombok.extern.slf4j.Slf4j;  // Importing Slf4j for logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j  // Enable logging for this class
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds an item to the cart.
     * Checks if the product is in stock and whether the user exists.
     * If the product already exists in the user's cart, it updates the quantity.
     *
     * @param request the CartRequest containing user and product information
     * @return a success message
     */
    @Override
    public String addToCart(CartRequest request) {
        log.info("Adding items to cart for userId: {}", request.getUserId());

        // Check if the user exists
        Optional<User> userOpt = userRepository.findById(request.getUserId());
       if (userOpt.isEmpty()) {
            log.error("User with ID {} not found!", request.getUserId());
            throw new EcommerceException("User not found!");
        }

        User user = userOpt.get();

        for (CartProductRequest productRequest : request.getProducts()) {
            Optional<Product> productOpt = productRepository.findById(productRequest.getProductId());
            if (productOpt.isEmpty()) {
                log.error("Product with ID {} not found!", productRequest.getProductId());
                continue; // Skip invalid products instead of stopping the whole process
            }

            Product product = productOpt.get();
            if (productRequest.getQuantity() > product.getStockQuantity()) {
                log.error("Requested quantity for product {} exceeds available stock.", product.getName());
                continue; // Skip if requested quantity exceeds stock
            }

            Optional<Cart> existingCartOpt = cartRepository.findByUserIdAndProductId(
                    request.getUserId(), productRequest.getProductId());

            if (existingCartOpt.isPresent()) {
                Cart cart = existingCartOpt.get();
                cart.setQuantity(cart.getQuantity() + productRequest.getQuantity());
                cartRepository.save(cart);
                log.info("Cart updated with new quantity for productId: {}", productRequest.getProductId());
            } else {
                Cart cart = new Cart(user, product, productRequest.getQuantity(), product.getPrice());
                cartRepository.save(cart);
                log.info("Item added to cart for productId: {}", productRequest.getProductId());
            }
        }

        return "Cart processed successfully!";
    }

//    @Override
//    public String addToCart(CartRequest request) {
//        log.info("Adding item to cart for userId: {} and productId: {}", request.getUserId(), request.getProductId());
//
//        // Check if the user exists
//        Optional<User> userOpt = userRepository.findById(request.getUserId());
//        // Check if the product exists
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//
//        if (userOpt.isEmpty()) {
//            log.error("User with ID {} not found!", request.getUserId());
//            throw new EcommerceException("User not found!");
//        }
//
//        if (productOpt.isEmpty()) {
//            log.error("Product with ID {} not found!", request.getProductId());
//            throw new EcommerceException("Product not found!");
//        }
//
//        Product product = productOpt.get();
//
//        // Prevent adding out-of-stock products
//        if (request.getQuantity() > product.getStockQuantity()) {
//            log.error("Requested quantity for product {} exceeds available stock.", product.getName());
//            throw new EcommerceException("Requested quantity exceeds available stock.");
//        }
//
//        // Check if the item already exists in the user's cart
//        Optional<Cart> existingCartOpt = cartRepository.findByUserIdAndProductId(
//                request.getUserId(), request.getProductId());
//
//        if (existingCartOpt.isPresent()) {
//            Cart cart = existingCartOpt.get();
//            // Update the existing cart item quantity
//            cart.setQuantity(cart.getQuantity() + request.getQuantity());
//            cartRepository.save(cart);
//            log.info("Cart updated with new quantity for userId: {} and productId: {}", request.getUserId(), request.getProductId());
//            return "Cart updated successfully!";
//        }
//
//        // Add new item to the cart if not present
//        Cart cart = new Cart(userOpt.get(), product, request.getQuantity(), product.getPrice());
//        cartRepository.save(cart);
//        log.info("Item added to cart for userId: {} and productId: {}", request.getUserId(), request.getProductId());
//
//        return "Item added to cart!";
//    }

    /**
     * Updates the quantity of a specific cart item.
     * Ensures the new quantity does not exceed available stock.
     *
     * @param cartId  the cart item ID to be updated
     * @param quantity the new quantity to be set
     * @return a success message
     */
    @Override
    public String updateCart(Integer cartId, Integer quantity) {
        log.info("Updating cart item with cartId: {} to new quantity: {}", cartId, quantity);

        // Check if the cart item exists
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) {
            log.error("Cart item with cartId: {} not found!", cartId);
            throw new EcommerceException("Cart item not found!");
        }

        Cart cart = cartOpt.get();

        // Ensure the requested quantity does not exceed available stock
        if (quantity > cart.getProduct().getStockQuantity()) {
            log.error("Requested quantity for product {} exceeds available stock.", cart.getProduct().getName());
            throw new EcommerceException("Requested quantity exceeds available stock.");
        }

        // Update the cart item quantity
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        log.info("Cart item with cartId: {} updated successfully.", cartId);

        return "Cart updated successfully!";
    }

    /**
     * Retrieves all cart items for a specific user.
     *
     * @param userId the ID of the user whose cart items are to be fetched
     * @return a list of CartResponse objects
     */
    @Override
    public List<CartResponse> getCartByUser(Integer userId) {
        log.info("Fetching cart items for userId: {}", userId);

        // Check if the user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.error("User with ID {} not found!", userId);
            throw new EcommerceException("User not found!");
        }

        // Fetch the user's cart items and map them to CartResponse
        List<CartResponse> cartItems = cartRepository.findByUser(userOpt.get()).stream()
                .map(cart -> new CartResponse(
                        cart.getId(),
                        cart.getUser().getId(),
                        cart.getProduct().getId(),
                        cart.getProductName(),
                        cart.getQuantity(),
                        cart.getPrice()
                ))
                .collect(Collectors.toList());

        log.info("Fetched {} cart items for userId: {}", cartItems.size(), userId);
        return cartItems;
    }

    /**
     * Deletes a cart item (soft delete).
     *
     * @param cartId the ID of the cart item to be deleted
     * @return a success message
     */
    @Override
    public String deleteCartItem(Integer cartId) {
        log.info("Deleting cart item with cartId: {}", cartId);

        // Check if the cart item exists
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) {
            log.error("Cart item with cartId: {} not found!", cartId);
            throw new EcommerceException("Cart item not found!");
        }

        // Soft delete the cart item (set 'deleted' flag)
        Cart cart = cartOpt.get();
        cart.setDeleted(true);  // Mark as deleted instead of removing it
        cartRepository.save(cart);
        log.info("Cart item with cartId: {} marked as deleted.", cartId);

        return "Cart item deleted!";
    }
}
