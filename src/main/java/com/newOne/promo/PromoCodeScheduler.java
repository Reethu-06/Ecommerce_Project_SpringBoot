
package com.newOne.promo;

import com.newOne.service.PromoCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j  // This annotation automatically creates a logger field in the class
@Component
public class PromoCodeScheduler {

    @Autowired
    private PromoCodeService promoCodeService;

    // Runs every hour (e.g., 1:00 AM, 2:00 AM, etc.)
    @Scheduled(cron = "0 0 * * * ?")
    public void checkAndExpirePromoCodes() {
        log.info("Scheduled task to check and expire promo codes started.");
        promoCodeService.markExpiredPromoCodes();
        log.info("Scheduled task to check and expire promo codes completed.");
    }
}
