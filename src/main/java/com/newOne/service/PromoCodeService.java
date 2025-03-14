package com.newOne.service;


import com.newOne.request.PromoCodeRequest;
import com.newOne.response.PromoCodeResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PromoCodeService {

  /**
   * Creates a new promo code in the system.
   *
   * @param request The promo code details (e.g., code, discount amount, expiration date).
   * @return A success or failure message after attempting to create the promo code.
   */
  String createPromoCode(PromoCodeRequest request);

  /**
   * Retrieves a list of all promo codes available in the system.
   *
   * @return A list of all promo codes in the system.
   */
  List<PromoCodeResponse> getAllPromoCodes();

  /**
   * Retrieves a list of valid promo codes for a product.
   * This method fetches only the promo codes that can be applied to the provided product.
   *
   * @return A list of valid promo codes for products.
   */
  List<PromoCodeResponse> getValidProductPromoCodes();

  /**
   * Retrieves a list of valid promo codes for an order.
   * This method fetches only the promo codes that can be applied based on the order's total amount.
   *
   * @return A list of valid promo codes for orders.
   */
  List<PromoCodeResponse> getValidOrderPromoCodes();

  /**
   * Applies a promo code to an order total.
   * This method calculates the discount amount based on the provided promo code and order amount.
   *
   * @param promoCode The promo code to apply.
   * @param totalAmount The total order amount before discount.
   * @return The new total amount after applying the promo code discount.
   */
  BigDecimal applyPromoCode(String promoCode, BigDecimal totalAmount);

  /**
   * Marks expired promo codes as inactive.
   * This method is called to update the status of expired promo codes in the system.
   * It could be called periodically (e.g., through a scheduler).
   */
  void markExpiredPromoCodes();
}