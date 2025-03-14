package com.newOne.promo;

public interface PromoStatus {

    // Status for active promo codes
    int ACTIVE = 1;

    // Status for inactive promo codes
    int INACTIVE = 2;

    // Status for promo codes that have expired due to their date being past
    int EXPIRED_DUE_TO_DATE = 3;

    // Status for promo codes that have been manually inactivated by an admin
    int INACTIVATED_BY_ADMIN = 4;
}
