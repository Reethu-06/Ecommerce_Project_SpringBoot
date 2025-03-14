package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.*;
import com.newOne.repository.*;
import com.newOne.request.OrderRequestDto;
import com.newOne.response.OrderResponse;
import com.newOne.response.OrderResponseDto;
import com.newOne.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    // Autowired Repositories for CRUD operations
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderAuditRepository orderAuditRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletAuditRepository walletAuditRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Method to handle the entire order checkout process.
     * - Fetches the user, cart items, calculates the total amount.
     * - Validates wallet balance, applies promo code, updates stock, creates necessary entries.
     *
     * @param orderRequestDto Order details like user info and address
     * @return OrderResponseDto with success message and order ID
     */
    @Transactional
    @Override
    public OrderResponseDto checkoutOrder(OrderRequestDto orderRequestDto) {
        int userId = orderRequestDto.getUserId();
        String promoCode = orderRequestDto.getPromoCode();

        // Step 1: Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        log.info("User fetched successfully: {}", user);

        // Step 2: Fetch Cart Items
        List<Cart> cartItems = cartRepository.findByUserId((long) userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot proceed with checkout.");
        }
        log.info("Fetched {} cart items for user ID: {}", cartItems.size(), userId);

        // Step 3: Calculate Total Amount
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Step 4: Wallet Check
        Wallet wallet = walletRepository.findByUserId((long) userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));

        if (wallet.getBalance() < totalAmount.intValue()) {
            transactionRepository.save(new Transaction(user, null, totalAmount, TransactionType.DEBIT,
                    TransactionStatus.FAILED, PaymentMethod.WALLET, "Insufficient wallet balance."));
            throw new RuntimeException("Insufficient wallet balance.");
        }

        // Step 5: Deduct Wallet Balance
        wallet.setBalance(wallet.getBalance() - totalAmount.intValue());
        walletRepository.save(wallet);

        walletAuditRepository.save(new WalletAudit(user, wallet, totalAmount, TransactionType.DEBIT, TransactionReason.PURCHASE));

        // Step 6: Address Handling
        Address address = resolveAddress(orderRequestDto, user);

        // Step 7: Calculate Discount if Promo Code is Provided
        BigDecimal discountAmount = (promoCode != null && !promoCode.isEmpty())
                ? calculateDiscount(promoCode, totalAmount, userId)
                : BigDecimal.ZERO;

        BigDecimal finalAmount = totalAmount.subtract(discountAmount).max(BigDecimal.ZERO);

        Orders order = new Orders();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setOrderStatus(statusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new RuntimeException("Status 'PENDING' not found.")));

        orderRepository.save(order);
        log.info("Order created with ID: {}", order.getId());

// Optionally log the address details
        log.info("Order address: {}, {}, {}, {}, {}",
                address.getStreet(), address.getCity(),
                address.getState(), address.getPostalCode(), address.getCountry());

        Orders savedOrder = orderRepository.save(order);
        // Step 9: Create Order Items & Update Stock
        for (Cart item : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setProductName(item.getProductName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItemRepository.save(orderItem);

            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Step 10: Order Audit Entry
        orderAuditRepository.save(new OrderAudit(order, null, statusRepository.findByStatusName("PROCESSING")
                .orElseThrow(() -> new EcommerceException("Status 'PROCESSING' not found.")), "Order Accepted"));

        // Step 11: Payment Entry
        paymentRepository.save(new Payment(order, totalAmount, PaymentStatus.SUCCESS));

        // Step 12: Successful Transaction
        transactionRepository.save(new Transaction(user, order, totalAmount, TransactionType.DEBIT,
                TransactionStatus.SUCCESS, PaymentMethod.WALLET, "Order Checkout Successful"));

        // Step 13: Clear User Cart
        cartRepository.deleteByUserId(userId);

        // Step 14: Response
        return new OrderResponseDto("Order placed successfully.", order.getId());
    }

    private Address resolveAddress(OrderRequestDto orderRequestDto, User user) {
        if (orderRequestDto.getStreet() != null && orderRequestDto.getCity() != null) {
            // New address provided, save it
            Address newAddress = new Address(
                    user,
                    orderRequestDto.getStreet(),
                    orderRequestDto.getCity(),
                    orderRequestDto.getState(),
                    orderRequestDto.getPostalCode(),
                    orderRequestDto.getCountry()
            );
            return addressRepository.save(newAddress);
        } else {
            // No new address, fetch the latest existing one
            return addressRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                    .orElseThrow(() -> new EcommerceException("No address found. Please provide an address."));
        }
    }


    /**
     * Method to calculate discount based on promo code type (ORDER or PRODUCT).
     *
     * @param promoCode Promo code entered by the user
     * @param totalAmount Total order amount
     * @param userId User ID
     * @return Calculated discount amount
     */
    private BigDecimal calculateDiscount(String promoCode, BigDecimal totalAmount, int userId) {
        PromoCode promo = promoCodeRepository.findByCode(promoCode)
                .orElseThrow(() -> new EcommerceException("Invalid promo code."));
        log.info("Promo code validated: {}", promoCode);

        if (promo.getValidFrom().isAfter(LocalDateTime.now()) || promo.getValidTo().isBefore(LocalDateTime.now())) {
            throw new EcommerceException("Promo code is expired or not yet active.");
        }

        if (promo.getPromoType() == PromoType.ORDER) {
            if (promo.getMinOrderAmount() != null && totalAmount.compareTo(promo.getMinOrderAmount()) < 0) {
                throw new EcommerceException("Order amount does not meet the minimum requirement for this promo code.");
            }
            return totalAmount.multiply(promo.getDiscountPercentage()).divide(BigDecimal.valueOf(100));
        } else if (promo.getPromoType() == PromoType.PRODUCT) {
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<Cart> cartItems = cartRepository.findByUserId((long) userId);
            for (Cart item : cartItems) {
                if (promo.getProduct() != null && promo.getProduct().getId() == item.getProduct().getId()) {
                    BigDecimal productDiscount = item.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                            .multiply(promo.getDiscountPercentage())
                            .divide(BigDecimal.valueOf(100));
                    discountAmount = discountAmount.add(productDiscount);
                }
            }
            if (discountAmount.compareTo(BigDecimal.ZERO) == 0) {
                throw new EcommerceException("Promo code does not apply to any items in the cart.");
            }
            return discountAmount;
        }

        throw new EcommerceException("Invalid promo code type.");
    }



    @Override
    public List<OrderResponse> getOrders(Long userId, Long orderId) {
        List<Orders> orders = (userId != null && orderId != null)
                ? orderRepository.findByUserIdAndId(userId, orderId)
                : (userId != null)
                ? orderRepository.findByUserId(Math.toIntExact(userId))
                : (orderId != null)
                ? orderRepository.findById(Math.toIntExact(orderId)).map(Collections::singletonList).orElse(Collections.emptyList())
                : orderRepository.findAll();

        log.info("Found {} orders", orders.size());
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Orders order) {
        return new OrderResponse(
                order.getUser().getId(),
                order.getTotalAmount(),
                order.getOrderStatus()
        );
    }

    @Transactional
    @Override
    public void cancelOrder(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcommerceException("Order not found with ID: " + orderId));

        // Validate order status before proceeding
        if (order.getOrderStatus().getId().equals("CANCELLED")) {
            throw new EcommerceException("Order is already cancelled.");
        }

        BigDecimal refundAmount = order.getTotalAmount().subtract(order.getDiscountAmount());

        // Refund to wallet
        Wallet wallet = walletRepository.findByUserId((long) order.getUser().getId())
                .orElseThrow(() -> new EcommerceException("Wallet not found"));

        // Add refund amount to wallet balance (using normal arithmetic)
        BigDecimal currentBalance = wallet.getBalance() != null
                ? BigDecimal.valueOf(wallet.getBalance())
                : BigDecimal.ZERO;

        wallet.setBalance(currentBalance.add(refundAmount).intValue());

        walletRepository.save(wallet);

        // Wallet Audit Entry
        walletAuditRepository.save(new WalletAudit(
                order.getUser(),
                wallet,
                refundAmount,
                TransactionType.CREDIT,
                TransactionReason.REFUND
        ));

        // Restore product stock from ORDER ITEMS
        List<OrderItems> orderItems = orderItemRepository.findByOrderId((long) orderId);
        for (OrderItems item : orderItems) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EcommerceException("Product not found"));
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // Update order status
        Status pastStatus = order.getOrderStatus();
        Status cancelledStatus = statusRepository.findByStatusName("CANCELLED")
                .orElseThrow(() -> new EcommerceException("Status 'CANCELLED' not found."));

        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setOrderStatus(cancelledStatus);
        orderRepository.save(order);

        // Create Transaction Entry
        transactionRepository.save(new Transaction(
                order.getUser(),
                order,
                refundAmount,
                TransactionType.CREDIT,
                TransactionStatus.SUCCESS,
                PaymentMethod.WALLET,
                "Order cancellation refund for Order ID: " + order.getId()
        ));

        // Create Order Audit Entry
        orderAuditRepository.save(new OrderAudit(
                order,
                pastStatus,
                cancelledStatus,
                "Order cancelled and refunded"
        ));
    }



    @Override
    @Transactional
    public OrderResponse updateOrder(int orderId, int newStatusId) {
        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcommerceException("Order not found with ID: " + orderId));

        // Store the previous status for audit tracking
        Status pastStatus = existingOrder.getOrderStatus();

        // Fetch the new status
        Status newStatus = statusRepository.findById((long) newStatusId)
                .orElseThrow(() -> new EcommerceException("Status not found with ID: " + newStatusId));

        // Update the order
        existingOrder.setOrderStatus(newStatus);
        orderRepository.save(existingOrder);
        log.info("Order with ID: {} status updated to: {}", orderId, newStatus.getStatusName());

        // Insert entry in order_audit
        OrderAudit orderAudit = new OrderAudit();
        orderAudit.setOrder(existingOrder);
        orderAudit.setPastStatus(pastStatus);
        orderAudit.setCurrentStatus(newStatus);
        orderAuditRepository.save(orderAudit);

        return new OrderResponse(
                existingOrder.getId(),
                existingOrder.getTotalAmount(),
                existingOrder.getOrderStatus()
        );
    }

}
