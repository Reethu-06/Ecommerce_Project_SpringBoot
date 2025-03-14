package com.newOne.repository;

import com.newOne.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing PromoCode entities.
 * Provides various methods to interact with PromoCodes, including methods for finding valid and expired promo codes.
 */
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {

    /**
     * Finds a PromoCode by its code, ensuring the promo code is not deleted.
     *
     * @param code The promo code to search for.
     * @return An Optional containing the PromoCode if found, otherwise empty.
     */
    Optional<PromoCode> findByCodeAndIsDeletedFalse(String code);

    /**
     * Checks if a PromoCode with the given code exists.
     *
     * @param code The promo code to check for existence.
     * @return true if the PromoCode exists, false otherwise.
     */
    boolean existsByCode(String code);

    /**
     * Finds valid order promo codes that can be applied to an order.
     *
     * @param orderAmount The order amount used to filter the valid promo codes.
     * @return A list of valid order promo codes.
     */
    @Query("SELECT p FROM PromoCode p WHERE p.promoType = com.newOne.entity.PromoType.ORDER " +
            "AND p.minOrderAmount <= :orderAmount " +
            "AND p.validFrom <= CURRENT_TIMESTAMP " +
            "AND p.validTo >= CURRENT_TIMESTAMP " +
            "AND p.isDeleted = false")
    List<PromoCode> findValidOrderPromoCodes(BigDecimal orderAmount);

    /**
     * Finds valid product promo codes that can be applied to a specific product.
     *
     * @param productId The product ID used to filter the valid promo codes.
     * @return A list of valid product promo codes.
     */
    @Query("SELECT p FROM PromoCode p WHERE p.promoType = com.newOne.entity.PromoType.PRODUCT " +
            "AND p.product.id = :productId " +
            "AND p.validFrom <= CURRENT_TIMESTAMP " +
            "AND p.validTo >= CURRENT_TIMESTAMP " +
            "AND p.isDeleted = false")
    List<PromoCode> findValidProductPromoCodes(Integer productId);

    /**
     * Finds a PromoCode by its code.
     *
     * @param code The promo code to search for.
     * @return An Optional containing the PromoCode if found, otherwise empty.
     */
    Optional<PromoCode> findByCode(String code);

    /**
     * Finds a PromoCode by its code, ensuring it is neither deleted nor archived.
     *
     * @param code The promo code to search for.
     * @return An Optional containing the PromoCode if found, otherwise empty.
     */
    Optional<PromoCode> findByCodeAndIsDeletedFalseAndIsArchivedFalse(String code);

    /**
     * Finds expired promo codes that have the given status and are no longer valid.
     *
     * @param status The status of the promo code (e.g., expired).
     * @return A list of expired promo codes.
     */
    @Query("SELECT p FROM PromoCode p WHERE p.status = :status AND p.validTo < CURRENT_TIMESTAMP AND p.isDeleted = false")
    List<PromoCode> findExpiredPromoCodes(@Param("status") int status);
}
