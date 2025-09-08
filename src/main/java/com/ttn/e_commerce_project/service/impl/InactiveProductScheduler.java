package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.respository.ProductRepository;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class InactiveProductScheduler {

    final ProductRepository productRepository;
    final EmailService emailService;

    @Value("${admin.default.email}")
    String adminEmail;

    @Scheduled(cron = "0 0 9 * * *")
    public void notifyAdminOfInactiveProducts() {
        log.info("Running scheduled job: Checking for recent inactive products...");

        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<Product> inactiveProducts = productRepository.findInactiveProductsCreatedSince(threshold);

        if (inactiveProducts.isEmpty()) {
            log.info("No recent inactive products found. No email will be sent.");
            return;
        }

        log.info("Found {} inactive products. Preparing email for admin.", inactiveProducts.size());

        StringBuilder emailBody = new StringBuilder("Hello Admin,\n\nThe following products were created in the last 24 hours but are not yet active. Please review them:\n\n");
        for (Product product : inactiveProducts) {
            emailBody.append("- ")
                    .append(product.getName())
                    .append(" (ID: ")
                    .append(product.getId())
                    .append(")\n");
        }
        emailBody.append("\nThank you.");

        emailService.sendProductInactiveMail(adminEmail, "Alert: Inactive Products Require Review", emailBody.toString());
        log.info("Inactive product report sent to admin at {}.", adminEmail);
    }
}
